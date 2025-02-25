package cn.ecosync.ibms.gateway.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.gateway.command.ReloadTelemetryServiceCommand;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.model.LabelTable;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.RelabelConfig;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.ScrapeConfig;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.ScrapeConfigs;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.StaticConfig;
import cn.ecosync.ibms.gateway.service.DeviceTelemetryService;
import cn.ecosync.ibms.gateway.service.GatewayMetricsTelemetryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static cn.ecosync.ibms.Constants.*;

public class ReloadTelemetryServiceCommandHandler implements CommandHandler<ReloadTelemetryServiceCommand> {
    private final GatewayMetricsTelemetryService gatewayMetricsTelemetryService;
    private final DeviceTelemetryService deviceTelemetryService;
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private final ObjectMapper yamlSerde;
    private final File scrapeConfigFile;
    private final Environment environment;

    public ReloadTelemetryServiceCommandHandler(
            GatewayMetricsTelemetryService gatewayMetricsTelemetryService,
            DeviceTelemetryService deviceTelemetryService,
            DeviceDataAcquisitionRepository dataAcquisitionRepository,
            Environment environment) {
        this.gatewayMetricsTelemetryService = gatewayMetricsTelemetryService;
        this.deviceTelemetryService = deviceTelemetryService;
        this.dataAcquisitionRepository = dataAcquisitionRepository;
        this.yamlSerde = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.scrapeConfigFile = new File("scrape_config_file.yml");
        this.environment = environment;
    }

    @Override
    @Transactional(readOnly = true)
    public synchronized void handle(ReloadTelemetryServiceCommand command) {
        DeviceDataAcquisition[] dataAcquisitions = dataAcquisitionRepository.search(Pageable.unpaged()).getContent()
                .toArray(new DeviceDataAcquisition[0]);
//        gatewayMetricsTelemetryService.reload(dataAcquisitions);
        deviceTelemetryService.reload(dataAcquisitions);

        List<ScrapeConfig> scrapeConfigs = new ArrayList<>();
        scrapeConfigs.add(jvmScrapeConfig());
//        scrapeConfigs.add(ibmsScrapeConfig());
        for (DeviceDataAcquisition dataAcquisition : dataAcquisitions) {
            ScrapeConfig scrapeConfig = toScrapeConfig(dataAcquisition);
            scrapeConfigs.add(scrapeConfig);
        }

        try {
            yamlSerde.writeValue(scrapeConfigFile, new ScrapeConfigs(scrapeConfigs));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ScrapeConfig jvmScrapeConfig() {
        String metricsPath = "/ibms" + PATH_METRICS_JVM;
        return new ScrapeConfig("jvm", metricsPath, null, null, null, new StaticConfig(getGatewayHost()));
    }

    private ScrapeConfig ibmsScrapeConfig() {
        String metricsPath = "/ibms" + PATH_METRICS;
        return new ScrapeConfig(getGatewayCode(), metricsPath, null, null, null, new StaticConfig(getGatewayHost()));
    }

    private ScrapeConfig toScrapeConfig(DeviceDataAcquisition dataAcquisition) {
        LabelTable dataPoints = dataAcquisition.getDataPoints();
        String[] deviceCodes = dataPoints.get(LABEL_DEVICE_CODE)
                .distinct()
                .toArray(String[]::new);
        String jobName = "ibms";
        StaticConfig staticConfig = new StaticConfig(deviceCodes);
        List<RelabelConfig> relabelConfigs = RelabelConfig.toRelabelConfigs(getGatewayHost());

        String metricsPath = "/ibms" + PATH_METRICS_DEVICES;
        Integer scrapeInterval = dataAcquisition.getScrapeInterval();
        Integer scrapeTimeout = dataAcquisition.getScrapeTimeout();
        return new ScrapeConfig(jobName, metricsPath, scrapeInterval, scrapeTimeout, relabelConfigs, staticConfig);
    }

    public String getGatewayHost() {
        return environment.getRequiredProperty("IBMS_HOST");
    }

    public String getGatewayCode() {
        return environment.getRequiredProperty("IBMS_GATEWAY_CODE");
    }
}
