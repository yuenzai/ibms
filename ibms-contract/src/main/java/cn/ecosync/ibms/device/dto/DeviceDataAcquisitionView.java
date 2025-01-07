package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.model.IDeviceDataAcquisition;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@ToString
public class DeviceDataAcquisitionView implements IDeviceDataAcquisition {
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
    private DeviceSchemas schemas;
    private Page<? extends Device> devices;

    protected DeviceDataAcquisitionView() {
    }

    public DeviceDataAcquisitionView(DeviceDataAcquisitionId dataAcquisitionId, DeviceSchemas schemas, Page<? extends Device> devices) {
        this.dataAcquisitionId = dataAcquisitionId;
        this.schemas = schemas;
        this.devices = devices;
    }

    @Override
    public DeviceSchemas getSchemas() {
        return schemas;
    }

    @Override
    public Page<? extends Device> getDevices() {
        return devices;
    }
}
