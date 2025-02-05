package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.gateway.PrometheusConfigurationProperties.RelabelConfig;
import cn.ecosync.ibms.gateway.PrometheusConfigurationProperties.ScrapeConfig;
import cn.ecosync.ibms.gateway.PrometheusConfigurationProperties.ScrapeConfigs;
import cn.ecosync.ibms.gateway.PrometheusConfigurationProperties.StaticConfig;
import cn.ecosync.ibms.util.CollectionUtils;
import cn.ecosync.ibms.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.prometheus.metrics.model.registry.MultiCollector;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.prometheus.metrics.model.registry.PrometheusScrapeRequest;
import io.prometheus.metrics.model.snapshots.MetricSnapshots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PrometheusTelemetryService implements MultiCollector {
    private static final Logger log = LoggerFactory.getLogger(PrometheusTelemetryService.class);
    public static final String PATH_METRICS = "/metrics";
    public static final String PATH_METRICS_DEVICES = "/metrics/devices";

    private final String serverPort;
    private final PrometheusService prometheusService;
    private final PrometheusRegistry deviceMetricsRegistry;
    private final Map<DeviceDataAcquisitionId, DeviceDataAcquisition> dataAcquisitionMap = new ConcurrentHashMap<>();
    private final ObjectMapper yamlSerde;
    private final File deviceScrapeConfigFile;
    private final Map<String, MultiCollector> instruments = new ConcurrentHashMap<>();

    public PrometheusTelemetryService(
            Environment environment, RestClient.Builder restClientBuilder,
            PrometheusRegistry deviceMetricsRegistry) {
        this.serverPort = environment.getProperty("server.port", "8080");
        String PROMETHEUS_ENDPOINT = environment.getProperty("PROMETHEUS_ENDPOINT", "localhost:9090");
        RestClient restClient = restClientBuilder.baseUrl("http://" + PROMETHEUS_ENDPOINT).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.prometheusService = factory.createClient(PrometheusService.class);
        this.deviceMetricsRegistry = deviceMetricsRegistry;
        this.yamlSerde = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.deviceScrapeConfigFile = new File("scrape_config_device.yml");
    }

    public void add(DeviceDataAcquisition deviceDataAcquisition) {
        dataAcquisitionMap.put(deviceDataAcquisition.getDataAcquisitionId(), deviceDataAcquisition);
    }

    public boolean remove(DeviceDataAcquisitionId deviceDataAcquisitionId) {
        return dataAcquisitionMap.remove(deviceDataAcquisitionId) != null;
    }

    public void reload() {
        Map<String, MultiCollector> instruments = new HashMap<>();
        List<ScrapeConfig> scrapeConfigs = new ArrayList<>();
        for (DeviceDataAcquisition dataAcquisition : dataAcquisitionMap.values()) {
            Set<String> deviceCodes = new HashSet<>();
            dataAcquisition.getDataPoints().newInstruments((deviceCode, instrument) -> {
                deviceCodes.add(deviceCode);
                instruments.put(deviceCode, instrument);
            });
            if (CollectionUtils.notEmpty(deviceCodes)) {
                String jobName = dataAcquisition.getDataAcquisitionId().toString();
                StaticConfig staticConfig = new StaticConfig(deviceCodes, Collections.singletonMap("target_type", "device"));
                List<RelabelConfig> relabelConfigs = RelabelConfig.toRelabelConfigs("localhost:" + serverPort);
                scrapeConfigs.add(new ScrapeConfig(jobName, PATH_METRICS_DEVICES, dataAcquisition.getScrapeInterval(), relabelConfigs, staticConfig));
            }
        }
        try {
            deviceMetricsRegistry.clear();
            this.instruments.clear();
            this.instruments.putAll(instruments);
            deviceMetricsRegistry.register(this);
            yamlSerde.writeValue(deviceScrapeConfigFile, new ScrapeConfigs(scrapeConfigs));
            prometheusService.reload();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MetricSnapshots collect(PrometheusScrapeRequest scrapeRequest) {
        String deviceCode = CollectionUtils.firstElement(Arrays.asList(scrapeRequest.getParameterValues("target")));
        log.info("collect(requestPath={}, target={})", scrapeRequest.getRequestPath(), deviceCode);
        return Optional.ofNullable(deviceCode)
                .filter(StringUtils::hasText)
                .map(instruments::get)
                .map(MultiCollector::collect)
                .orElseGet(this::collect);
    }

    @Override
    public MetricSnapshots collect() {
        return new MetricSnapshots();
    }
}
