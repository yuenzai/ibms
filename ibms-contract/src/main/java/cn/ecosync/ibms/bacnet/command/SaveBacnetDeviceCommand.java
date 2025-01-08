package cn.ecosync.ibms.bacnet.command;

import cn.ecosync.ibms.bacnet.model.BacnetDevice;
import cn.ecosync.ibms.device.command.SaveDeviceCommand;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SaveBacnetDeviceCommand implements SaveDeviceCommand {
    @Valid
    @NotNull
    @JsonUnwrapped
    private DeviceId deviceId;
    private String deviceName;
    @NotNull
    private Integer deviceInstance;

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public BacnetDevice toDevice(DeviceSchemasId schemasId) {
        return new BacnetDevice(deviceId, schemasId, deviceName, deviceInstance);
    }
}
