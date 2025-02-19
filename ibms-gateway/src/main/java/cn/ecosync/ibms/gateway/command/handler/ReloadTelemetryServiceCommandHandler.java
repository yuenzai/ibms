package cn.ecosync.ibms.gateway.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.gateway.command.ReloadTelemetryServiceCommand;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.model.LabelTable;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.RelabelConfig;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.ScrapeConfig;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.ecosync.ibms.Constants.*;

public class ReloadTelemetryServiceCommandHandler implements CommandHandler<ReloadTelemetryServiceCommand> {
    private final GatewayMetricsTelemetryService gatewayMetricsTelemetryService;
    private final DeviceTelemetryService deviceTelemetryService;
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private final ObjectMapper yamlSerde;
    private final String gatewayHost;
    private final String gatewayCode;
    private final File scrapeConfigFile;

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
        this.gatewayHost = environment.getRequiredProperty("IBMS_HOST");
        this.gatewayCode = environment.getRequiredProperty("IBMS_GATEWAY_CODE");
        this.scrapeConfigFile = new File("scrape_config_file.yml");
    }

    @Override
    @Transactional(readOnly = true)
    public void handle(ReloadTelemetryServiceCommand command) {
        DeviceDataAcquisition[] dataAcquisitions = dataAcquisitionRepository.search(Pageable.unpaged()).getContent()
                .toArray(new DeviceDataAcquisition[0]);
        gatewayMetricsTelemetryService.reload(dataAcquisitions);
        deviceTelemetryService.reload(dataAcquisitions);

        List<ScrapeConfig> scrapeConfigs = new ArrayList<>();
        ScrapeConfig gatewayScrapeConfig = new ScrapeConfig(gatewayCode, "/ibms" + PATH_METRICS, 30, new StaticConfig(gatewayHost));
        scrapeConfigs.add(gatewayScrapeConfig);
        for (DeviceDataAcquisition dataAcquisition : dataAcquisitions) {
            ScrapeConfig scrapeConfig = toScrapeConfig(dataAcquisition);
            scrapeConfigs.add(scrapeConfig);
        }

        try {
            yamlSerde.writeValue(scrapeConfigFile, new PrometheusConfigurationProperties.ScrapeConfigs(scrapeConfigs));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ScrapeConfig toScrapeConfig(DeviceDataAcquisition dataAcquisition) {
        LabelTable dataPoints = dataAcquisition.getDataPoints();
        Set<String> deviceCodes = dataPoints.get(LABEL_DEVICE_CODE)
                .collect(Collectors.toSet());
        String jobName = dataAcquisition.getDataAcquisitionId().toString();
        StaticConfig staticConfig = new StaticConfig(deviceCodes, Collections.singletonMap("target_type", "device"));
        List<RelabelConfig> relabelConfigs = RelabelConfig.toRelabelConfigs(gatewayHost);
        return new ScrapeConfig(jobName, "/ibms" + PATH_METRICS_DEVICES, dataAcquisition.getScrapeInterval(), relabelConfigs, staticConfig);
    }
}
