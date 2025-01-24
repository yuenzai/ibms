package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataPoint;
import cn.ecosync.ibms.device.model.DeviceDataPointLabel;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.metrics.Instrument;
import cn.ecosync.ibms.metrics.PrometheusConfigurationProperties.RelabelConfig;
import cn.ecosync.ibms.metrics.PrometheusConfigurationProperties.ScrapeConfig;
import cn.ecosync.ibms.metrics.PrometheusConfigurationProperties.ScrapeConfigs;
import cn.ecosync.ibms.metrics.PrometheusConfigurationProperties.StaticConfig;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.util.CollectionUtils;
import cn.ecosync.iframework.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.prometheus.metrics.model.registry.MultiCollector;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.prometheus.metrics.model.registry.PrometheusScrapeRequest;
import io.prometheus.metrics.model.snapshots.*;
import io.prometheus.metrics.model.snapshots.GaugeSnapshot.GaugeDataPointSnapshot;
import io.prometheus.metrics.model.snapshots.InfoSnapshot.InfoDataPointSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class PrometheusTelemetryService implements MultiCollector {
    private static final Logger log = LoggerFactory.getLogger(PrometheusTelemetryService.class);
    public static final String PATH_METRICS = "/metrics";
    public static final String PATH_METRICS_DEVICES = "/metrics/devices";
    public static final String POINT_INFO_METRIC_NAME = "point";

    private final String serverPort;
    private final PrometheusService prometheusService;
    private final PrometheusRegistry deviceMetricsRegistry;
    private final JsonSerde jsonSerde;
    private final AtomicReference<DeviceGateway> gatewayRef = new AtomicReference<>();
    private final ObjectMapper yamlSerde;
    private final File deviceScrapeConfigFile;
    private final Map<String, Instrument> instruments = new ConcurrentHashMap<>();
    private final Set<String> prometheusNames = ConcurrentHashMap.newKeySet();

    public PrometheusTelemetryService(
            Environment environment, RestClient.Builder restClientBuilder,
            PrometheusRegistry deviceMetricsRegistry, JsonSerde jsonSerde) {
        this.serverPort = environment.getProperty("server.port", "8080");
        String PROMETHEUS_ENDPOINT = environment.getProperty("PROMETHEUS_ENDPOINT", "localhost:9090");
        RestClient restClient = restClientBuilder.baseUrl("http://" + PROMETHEUS_ENDPOINT).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.prometheusService = factory.createClient(PrometheusService.class);
        this.deviceMetricsRegistry = deviceMetricsRegistry;
        this.jsonSerde = jsonSerde;
        this.yamlSerde = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.deviceScrapeConfigFile = new File("scrape_config_device.yml");
    }

    public void reload(DeviceGateway gateway) {
        log.info("同步网关配置成功: {}", gateway);
        clear();
        gatewayRef.set(gateway);
        DeviceGateway gatewayAtomic = gatewayRef.get();
        if (gatewayAtomic == null || CollectionUtils.isEmpty(gatewayAtomic.getDataAcquisitions())) return;
        deviceMetricsRegistry.register(this);

        List<ScrapeConfig> scrapeConfigs = new ArrayList<>();
        for (DeviceDataAcquisition dataAcquisition : gatewayAtomic.getDataAcquisitions()) {
            Set<String> deviceCodes = new HashSet<>();
            dataAcquisition.newInstruments(jsonSerde, (deviceCode, dataPoints) -> {
                deviceCodes.add(deviceCode);
                instruments.put(deviceCode, dataPoints);
            });
            dataAcquisition.getDataPoints().stream()
                    .map(in -> in.getDataPointId().getMetricName())
                    .forEach(prometheusNames::add);
            prometheusNames.add(POINT_INFO_METRIC_NAME);
            if (CollectionUtils.notEmpty(deviceCodes)) {
                String jobName = dataAcquisition.getDataAcquisitionId().toString();
                StaticConfig staticConfig = new StaticConfig(deviceCodes);
                List<RelabelConfig> relabelConfigs = RelabelConfig.toRelabelConfigs("localhost:" + serverPort);
                scrapeConfigs.add(new ScrapeConfig(jobName, PATH_METRICS_DEVICES, dataAcquisition.getScrapeInterval(), relabelConfigs, staticConfig));
            }
        }

        try {
            yamlSerde.writeValue(deviceScrapeConfigFile, new ScrapeConfigs(scrapeConfigs));
            prometheusService.reload();
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public MetricSnapshots collect(PrometheusScrapeRequest scrapeRequest) {
        String deviceCode = CollectionUtils.firstElement(Arrays.asList(scrapeRequest.getParameterValues("target")));
        log.info("collect(requestPath={}, target={})", scrapeRequest.getRequestPath(), deviceCode);
        Instrument instrument = Optional.ofNullable(deviceCode)
                .filter(StringUtils::hasText)
                .map(instruments::get)
                .orElse(null);
        if (instrument == null) return collect();

        PrometheusMetricsCollector metricsCollector = new PrometheusMetricsCollector();
        instrument.collectMetrics(metricsCollector);

        InfoSnapshot.Builder pointInfoMetricBuilder = InfoSnapshot.builder()
                .name(POINT_INFO_METRIC_NAME)
                .help("Point Info");
        instrument.getDataPoints().stream()
                .map(this::toLabels)
                .map(InfoDataPointSnapshot::new)
                .forEach(pointInfoMetricBuilder::dataPoint);
        InfoSnapshot pointInfoMetric = pointInfoMetricBuilder.build();
        metricsCollector.addMetric(pointInfoMetric);
        return metricsCollector.toMetricSnapshots();
    }

    private Labels toLabels(DeviceDataPoint dataPoint) {
        Labels.Builder builder = Labels.builder();
        builder.label("point_name", dataPoint.getDataPointId().getPointName());
        for (DeviceDataPointLabel label : dataPoint.getLabels()) {
            builder.label(PrometheusNaming.sanitizeMetricName(label.getName()), label.getValue());
        }
        return builder.build();
    }

    private void clear() {
        instruments.clear();
        prometheusNames.clear();
        deviceMetricsRegistry.clear();
    }

    @Override
    public MetricSnapshots collect() {
        return new MetricSnapshots();
    }

    @Override
    public List<String> getPrometheusNames() {
        return new ArrayList<>(prometheusNames);
    }

    public static class PrometheusMetricsCollector implements Instrument.MetricsCollector {
        private final MetricSnapshots.Builder metricsBuilder = MetricSnapshots.builder();

        @Override
        public void collect(String metricName, double value, DeviceDataPointLabel... labels) {
            Labels.Builder labelsBuilder = Labels.builder();
            for (DeviceDataPointLabel label : labels) {
                labelsBuilder.label(label.getName(), label.getValue());
            }
            GaugeDataPointSnapshot dataPoint = new GaugeDataPointSnapshot(value, labelsBuilder.build(), null);
            GaugeSnapshot metric = GaugeSnapshot.builder()
                    .name(metricName)
                    .dataPoint(dataPoint)
                    .build();
            metricsBuilder.metricSnapshot(metric);
        }

        private void addMetric(MetricSnapshot metric) {
            metricsBuilder.metricSnapshot(metric);
        }

        public MetricSnapshots toMetricSnapshots() {
            return metricsBuilder.build();
        }
    }
}
