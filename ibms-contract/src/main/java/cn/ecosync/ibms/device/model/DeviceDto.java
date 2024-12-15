package cn.ecosync.ibms.device.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;

@ToString
public class DeviceDto implements DeviceModel {
    @JsonUnwrapped
    private DeviceId deviceId;
    @JsonUnwrapped
    private DeviceDataAcquisitionId daqId;
    @JsonUnwrapped
    private DeviceProperties deviceProperties;

    protected DeviceDto() {
    }

    public DeviceDto(DeviceId deviceId, DeviceProperties deviceProperties) {
        this.deviceId = deviceId;
        this.daqId = deviceId.toDaqId();
        this.deviceProperties = deviceProperties;
    }

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public DeviceDataAcquisitionId getDaqId() {
        return daqId;
    }

    @Override
    public DeviceProperties getDeviceProperties() {
        return deviceProperties;
    }
}
