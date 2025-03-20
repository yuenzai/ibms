package cn.ecosync.ibms.gateway.service;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.model.LabelTable;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.*;
import cn.ecosync.ibms.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cn.ecosync.ibms.Constants.*;

public class GatewayApplicationService {
    private final DeviceTelemetryService deviceTelemetryService;
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private final ObjectMapper yamlSerde;
    private final File prometheusConfigFile;
    private final Environment environment;

    public GatewayApplicationService(DeviceTelemetryService deviceTelemetryService, DeviceDataAcquisitionRepository dataAcquisitionRepository, Environment environment) {
        this.deviceTelemetryService = deviceTelemetryService;
        this.dataAcquisitionRepository = dataAcquisitionRepository;
        this.yamlSerde = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.prometheusConfigFile = new File("prometheus.yml");
        this.environment = environment;
    }

    public synchronized void reloadTelemetryService() {
        DeviceDataAcquisition[] dataAcquisitions = dataAcquisitionRepository.search(Pageable.unpaged()).getContent()
                .toArray(new DeviceDataAcquisition[0]);
        deviceTelemetryService.reload(dataAcquisitions);

        String gatewayCode = environment.getRequiredProperty("GATEWAY_CODE");
        String ibmsOrigin = environment.getRequiredProperty("IBMS_ORIGIN");

        Prometheus.Builder builder = Prometheus.builder()
                .withGlobal(new Global(Collections.singletonMap("gateway_code", gatewayCode)))
                .addRemoteWrite(new RemoteWrite(ibmsOrigin + "/ibms/data/prometheus/api/v1/write", null, Collections.singletonMap("Gateway-Code", gatewayCode)))
                .addScrapeConfig(jvmScrapeConfig())
                .addScrapeConfig(ScrapeConfig.NODE_EXPORTER);

        Arrays.stream(dataAcquisitions)
                .map(this::toScrapeConfig)
                .forEach(builder::addScrapeConfig);

        Prometheus prometheus = builder.build();

        try {
            yamlSerde.writeValue(prometheusConfigFile, prometheus);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ScrapeConfig jvmScrapeConfig() {
        String metricsPath = "/ibms" + PATH_METRICS_JVM;
        return new ScrapeConfig("jvm", metricsPath, new StaticConfig(getGatewayHost()));
    }

    private ScrapeConfig toScrapeConfig(DeviceDataAcquisition dataAcquisition) {
        LabelTable dataPoints = dataAcquisition.getDataPoints();
        String[] deviceCodes = dataPoints.get(LABEL_DEVICE_CODE)
                .distinct()
                .toArray(String[]::new);
        String jobName = "ibms";
        StaticConfig staticConfig = new StaticConfig(deviceCodes);
        List<RelabelConfig> relabelConfigs = RelabelConfig.toRelabelConfigs("device_code", getGatewayHost());

        String metricsPath = "/ibms" + PATH_METRICS_DEVICES;
        Integer scrapeInterval = dataAcquisition.getScrapeInterval();
        Integer scrapeTimeout = dataAcquisition.getScrapeTimeout();
        return new ScrapeConfig(jobName, metricsPath, true, scrapeInterval, scrapeTimeout, relabelConfigs, staticConfig);
    }

    public String getGatewayHost() {
        return Optional.ofNullable(environment.getProperty("GATEWAY_HOST")).filter(StringUtils::hasText).orElse("localhost:8080");
    }
}
