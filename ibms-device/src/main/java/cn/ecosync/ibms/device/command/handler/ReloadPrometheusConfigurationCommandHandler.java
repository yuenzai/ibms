package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.ReloadPrometheusConfigurationCommand;
import cn.ecosync.ibms.device.dto.PrometheusConfigurationProperties.RelabelConfig;
import cn.ecosync.ibms.device.dto.PrometheusConfigurationProperties.ScrapeConfig;
import cn.ecosync.ibms.device.dto.PrometheusConfigurationProperties.ScrapeConfigs;
import cn.ecosync.ibms.device.dto.PrometheusConfigurationProperties.StaticConfig;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.service.PrometheusService;
import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReloadPrometheusConfigurationCommandHandler implements CommandHandler<ReloadPrometheusConfigurationCommand> {
    public static final String PATH_METRICS_DEVICES = "/metrics/devices";

    private final PrometheusService prometheusService;
    private final ObjectMapper yamlSerde;
    private final File deviceScrapeConfigFile;
    private final String serverPort;

    public ReloadPrometheusConfigurationCommandHandler(PrometheusService prometheusService, Environment environment) {
        this.prometheusService = prometheusService;
        this.yamlSerde = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        this.deviceScrapeConfigFile = new File("scrape_config_device.yml");//todo
        this.serverPort = environment.getProperty("server.port", "8080");
    }

    @Override
    public void handle(ReloadPrometheusConfigurationCommand command) {
        List<DeviceDataAcquisition> dataAcquisitions = command.getDataAcquisitions();
        List<ScrapeConfig> scrapeConfigs = dataAcquisitions.stream()
                .map(this::toScrapeConfig)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        try {
            yamlSerde.writeValue(deviceScrapeConfigFile, new ScrapeConfigs(scrapeConfigs));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        prometheusService.reload();
    }

    private ScrapeConfig toScrapeConfig(DeviceDataAcquisition dataAcquisition) {
        Set<String> deviceCodes = dataAcquisition.getDataPoints()
                .toCollection().stream()
                .map(in -> in.getDataPointId().getDeviceCode())
                .collect(Collectors.toSet());
        if (CollectionUtils.notEmpty(deviceCodes)) {
            String jobName = dataAcquisition.getDataAcquisitionId().toString();
            StaticConfig staticConfig = new StaticConfig(deviceCodes, Collections.singletonMap("target_type", "device"));
            List<RelabelConfig> relabelConfigs = RelabelConfig.toRelabelConfigs("localhost:" + serverPort);
            return new ScrapeConfig(jobName, PATH_METRICS_DEVICES, dataAcquisition.getScrapeInterval(), relabelConfigs, staticConfig);
        }
        return null;
    }
}
