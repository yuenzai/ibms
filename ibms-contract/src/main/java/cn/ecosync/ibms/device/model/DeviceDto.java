package cn.ecosync.ibms.device.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.ToString;

@ToString
public class DeviceDto implements DeviceModel {
    @Valid
    @JsonUnwrapped
    private DeviceId deviceId;
    @Valid
    @JsonUnwrapped
    private DeviceProperties deviceProperties;

    protected DeviceDto() {
    }

    public DeviceDto(DeviceId deviceId, DeviceProperties deviceProperties) {
        this.deviceId = deviceId;
        this.deviceProperties = deviceProperties;
    }

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public DeviceProperties getDeviceProperties() {
        return deviceProperties;
    }
}
