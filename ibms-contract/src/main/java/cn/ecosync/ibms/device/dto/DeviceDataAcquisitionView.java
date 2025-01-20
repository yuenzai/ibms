package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.model.IDeviceDataAcquisition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

@Getter
@ToString
public class DeviceDataAcquisitionView implements IDeviceDataAcquisition {
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private Long scrapeInterval;
    private DeviceSchemas schemas;
    private Page<? extends Device> devices;

    protected DeviceDataAcquisitionView() {
    }

    public DeviceDataAcquisitionView(DeviceDataAcquisitionId dataAcquisitionId, Long scrapeInterval, DeviceSchemas schemas, Page<? extends Device> devices) {
        this.dataAcquisitionId = dataAcquisitionId;
        this.scrapeInterval = scrapeInterval;
        this.schemas = schemas;
        this.devices = devices;
    }

    @Override
    public Long getScrapeInterval() {
        return scrapeInterval;
    }

    @Override
    public DeviceSchemas getSchemas() {
        return schemas;
    }

    @Override
    @JsonIgnore
    public Page<? extends Device> getDevices() {
        return devices;
    }

    @JsonProperty("devices")
    public PagedModel<? extends Device> devices() {
        return new PagedModel<>(devices);
    }
}
