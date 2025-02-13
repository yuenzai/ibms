package cn.ecosync.ibms.gateway.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.gateway.command.PushGatewayConfigurationCommand;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.RelabelConfig;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.ScrapeConfig;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.ScrapeConfigs;
import cn.ecosync.ibms.gateway.model.PrometheusConfigurationProperties.StaticConfig;
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

public class PushGatewayConfigurationCommandHandler implements CommandHandler<PushGatewayConfigurationCommand> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private final ObjectMapper yamlSerde;
    private final String gatewayHost;
    private final String gatewayCode;
    private final File scrapeConfigFile;

    public PushGatewayConfigurationCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, Environment environment) {
        this.dataAcquisitionRepository = dataAcquisitionRepository;
        this.yamlSerde = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.gatewayHost = environment.getRequiredProperty("IBMS_HOST");
        this.gatewayCode = environment.getRequiredProperty("IBMS_GATEWAY_CODE");
        this.scrapeConfigFile = new File("/opt/app/data/scrape_config_file.yml");
    }

    @Override
    @Transactional(readOnly = true)
    public void handle(PushGatewayConfigurationCommand command) {
        List<DeviceDataAcquisition> dataAcquisitions = dataAcquisitionRepository.search(Pageable.unpaged()).getContent();
        List<ScrapeConfig> scrapeConfigs = new ArrayList<>(dataAcquisitions.size() + 1);
        ScrapeConfig gatewayScrapeConfig = new ScrapeConfig(gatewayCode, "/metrics", 30, new StaticConfig(gatewayHost));
        scrapeConfigs.add(gatewayScrapeConfig);
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

    private ScrapeConfig toScrapeConfig(DeviceDataAcquisition dataAcquisition) {
        Set<String> deviceCodes = dataAcquisition.getDataPoints()
                .toCollection().stream()
                .map(in -> in.getDataPointId().getDeviceCode())
                .collect(Collectors.toSet());
        String jobName = dataAcquisition.getDataAcquisitionId().toString();
        StaticConfig staticConfig = new StaticConfig(deviceCodes, Collections.singletonMap("target_type", "device"));
        List<RelabelConfig> relabelConfigs = RelabelConfig.toRelabelConfigs(gatewayHost);
        return new ScrapeConfig(jobName, "/metrics/devices", dataAcquisition.getScrapeInterval(), relabelConfigs, staticConfig);
    }
}
