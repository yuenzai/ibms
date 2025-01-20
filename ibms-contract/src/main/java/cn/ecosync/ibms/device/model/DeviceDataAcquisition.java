package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition;
import cn.ecosync.ibms.metrics.PrometheusConfigurationProperties.RelabelConfig;
import cn.ecosync.ibms.metrics.PrometheusConfigurationProperties.ScrapeConfig;
import cn.ecosync.ibms.metrics.PrometheusConfigurationProperties.StaticConfig;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = BacnetDataAcquisition.class, name = "BACNET"))
public abstract class DeviceDataAcquisition implements IDeviceDataAcquisition {
    @Valid
    @NotNull
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private Long scrapeInterval;

    protected DeviceDataAcquisition() {
    }

    protected DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        this.dataAcquisitionId = dataAcquisitionId;
    }

    @Override
    public Long getScrapeInterval() {
        return scrapeInterval;
    }

    @Override
    public abstract DeviceSchemas getSchemas();

    @Override
    public abstract List<? extends Device> getDevices();

    public abstract DeviceDataAcquisition addDeviceReferences(List<Device> devices);

    public abstract DeviceDataAcquisition removeDeviceReferences(List<Device> devices);

    public abstract DeviceDataAcquisition withSchemas(DeviceSchemas schemas);

    public abstract DeviceDataAcquisition withDevices(List<? extends Device> devices);

    public abstract DeviceDataAcquisition toReference();

    public ScrapeConfig toScrapeConfig(String metricsPath, String replacement) {
        List<String> targets = getDevices().stream()
                .map(Device::getDeviceId)
                .map(DeviceId::toString)
                .collect(Collectors.toList());
        String jobName = getDataAcquisitionId().toString();
        StaticConfig staticConfig = new StaticConfig(targets);
        List<RelabelConfig> relabelConfigs = RelabelConfig.toRelabelConfigs(replacement);
        return new ScrapeConfig(jobName, metricsPath, getScrapeInterval(), relabelConfigs, staticConfig);
    }
}
