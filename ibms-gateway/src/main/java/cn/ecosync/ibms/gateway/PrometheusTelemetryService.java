package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.bacnet.dto.*;
import cn.ecosync.ibms.bacnet.model.BacnetDevice;
import cn.ecosync.ibms.bacnet.model.BacnetSchema;
import cn.ecosync.ibms.bacnet.model.BacnetSchemas;
import cn.ecosync.ibms.bacnet.service.BacnetService;
import cn.ecosync.ibms.device.dto.DeviceSchema;
import cn.ecosync.ibms.device.model.*;
import cn.ecosync.ibms.metrics.PrometheusConfigurationProperties.ScrapeConfig;
import cn.ecosync.ibms.metrics.PrometheusConfigurationProperties.ScrapeConfigs;
import cn.ecosync.iframework.util.CollectionUtils;
import cn.ecosync.iframework.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.prometheus.metrics.model.registry.MultiCollector;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.prometheus.metrics.model.registry.PrometheusScrapeRequest;
import io.prometheus.metrics.model.snapshots.GaugeSnapshot;
import io.prometheus.metrics.model.snapshots.GaugeSnapshot.GaugeDataPointSnapshot;
import io.prometheus.metrics.model.snapshots.Labels;
import io.prometheus.metrics.model.snapshots.MetricSnapshot;
import io.prometheus.metrics.model.snapshots.MetricSnapshots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PrometheusTelemetryService implements MultiCollector {
    private static final Logger log = LoggerFactory.getLogger(PrometheusTelemetryService.class);
    public static final String PATH_METRICS = "/metrics";
    public static final String PATH_METRICS_DEVICES = "/metrics/devices";

    private final String serverPort;
    private final PrometheusService prometheusService;
    private final PrometheusRegistry deviceMetricsRegistry;
    private final BacnetService bacnetService;
    private final AtomicReference<DeviceGateway> gatewayRef = new AtomicReference<>();
    private final ObjectMapper yamlSerde;
    private final File deviceScrapeConfigFile;

    public PrometheusTelemetryService(
            Environment environment, RestClient.Builder restClientBuilder,
            PrometheusRegistry deviceMetricsRegistry, BacnetService bacnetService) {
        this.serverPort = environment.getProperty("server.port", "8080");
        String PROMETHEUS_ENDPOINT = environment.getProperty("PROMETHEUS_ENDPOINT", "localhost:9090");
        RestClient restClient = restClientBuilder.baseUrl("http://" + PROMETHEUS_ENDPOINT).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.prometheusService = factory.createClient(PrometheusService.class);
        this.deviceMetricsRegistry = deviceMetricsRegistry;
        this.bacnetService = bacnetService;
        this.yamlSerde = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.deviceScrapeConfigFile = new File("scrape_config_device.yml");
    }

    private final Map<String, Device> deviceMap = new ConcurrentHashMap<>();
    private final Map<String, DeviceSchemas> deviceSchemasMap = new ConcurrentHashMap<>();

    public void reload(DeviceGateway gateway) {
        log.info("同步网关配置成功: {}", gateway);
        clear();
        gatewayRef.set(gateway);
        DeviceGateway gatewayAtomic = gatewayRef.get();
        if (gatewayAtomic == null || CollectionUtils.isEmpty(gatewayAtomic.getDataAcquisitions())) return;
        deviceMetricsRegistry.register(this);

        gatewayAtomic.getDataAcquisitions().stream()
                .flatMap(in -> in.getDevices().stream())
                .forEach(in -> deviceMap.put(in.getDeviceId().toString(), in));
        gatewayAtomic.getDataAcquisitions().stream()
                .map(DeviceDataAcquisition::getSchemas)
                .forEach(in -> deviceSchemasMap.put(in.getSchemasId().toString(), in));
        try {
            updateScrapeConfigFile(gatewayAtomic);
            prometheusService.reload();
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    private void updateScrapeConfigFile(DeviceGateway gateway) throws IOException {
        List<ScrapeConfig> scrapeConfigs = gateway.getDataAcquisitions().stream()
                .filter(in -> !CollectionUtils.isEmpty(in.getDevices()))
                .map(in -> in.toScrapeConfig(PATH_METRICS_DEVICES, "localhost:" + serverPort))
                .collect(Collectors.toList());
        yamlSerde.writeValue(deviceScrapeConfigFile, new ScrapeConfigs(scrapeConfigs));
    }

    @Override
    public MetricSnapshots collect() {
        return new MetricSnapshots();
    }

    @Override
    public MetricSnapshots collect(PrometheusScrapeRequest scrapeRequest) {
        String targetName = CollectionUtils.firstElement(Arrays.asList(scrapeRequest.getParameterValues("target")));
        log.info("collect(requestPath={}, target={})", scrapeRequest.getRequestPath(), targetName);
        Device device = Optional.ofNullable(targetName)
                .filter(StringUtils::hasText)
                .map(deviceMap::get)
                .orElse(null);
        if (device == null) return collect();

        if (device instanceof BacnetDevice) {
            BacnetDevice bacnetDevice = (BacnetDevice) device;
            return collect(bacnetDevice);
        }
        return collect();
    }

    private MetricSnapshots collect(BacnetDevice bacnetDevice) {
        DeviceSchemasId schemasId = bacnetDevice.getSchemasId();
        BacnetSchemas bacnetSchemas = (BacnetSchemas) deviceSchemasMap.get(schemasId.toString());
        if (bacnetSchemas == null) return collect();

        ReadPropertyMultipleAck ack = bacnetService.scrape(bacnetDevice, bacnetSchemas);

        List<BacnetSchema> schemas = bacnetSchemas.getSchemas();
        Map<BacnetObject, Map<BacnetProperty, BacnetPropertyResult>> propertiesMap = ack.toMap();

        Collection<MetricSnapshot> metrics = new ArrayList<>();
        for (BacnetSchema schema : schemas) {
            BacnetObjectProperties objectProperties = schema.getSchemaProperties();
            Map<BacnetProperty, BacnetPropertyResult> propertyMap = propertiesMap.get(objectProperties.getBacnetObject());
            BacnetPropertyValue presentValue = Optional.ofNullable(propertyMap.get(BacnetProperty.PROPERTY_PRESENT_VALUE))
                    .map(BacnetPropertyResult::getValue)
                    .orElse(null);
            if (presentValue == null) continue;

            double value = presentValue.getValueAsNumber().doubleValue();
            GaugeDataPointSnapshot gaugeDataPointSnapshot = new GaugeDataPointSnapshot(value, Labels.EMPTY, null);
            GaugeSnapshot gaugeSnapshot = GaugeSnapshot.builder()
                    .name(schema.getName())
                    .dataPoint(gaugeDataPointSnapshot)
                    .build();
            metrics.add(gaugeSnapshot);
        }
        return new MetricSnapshots(metrics);
    }

    @Override
    public List<String> getPrometheusNames() {
        DeviceGateway gateway = gatewayRef.get();
        if (gateway == null) return MultiCollector.super.getPrometheusNames();
        return gateway.getDataAcquisitions().stream()
                .flatMap(in -> in.getSchemas().getSchemas().stream())
                .map(DeviceSchema::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    private void clear() {
        deviceMap.clear();
        deviceSchemasMap.clear();
        deviceMetricsRegistry.clear();
    }
}
