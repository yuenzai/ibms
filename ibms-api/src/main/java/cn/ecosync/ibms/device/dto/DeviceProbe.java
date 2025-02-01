package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.model.IDevice;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@ToString
public class DeviceProbe implements IDevice {
    @Size(min = 1)
    private String schemasCode;
    @NotNull
    private String deviceName;

    public DeviceSchemasId toSchemasId() {
        return Optional.ofNullable(schemasCode)
                .map(DeviceSchemasId::new)
                .orElse(null);
    }
}
