package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.model.IDevice;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DeviceProbe implements IDevice {
    @Size(min = 1)
    private String schemasCode;
    @Size(min = 1)
    private String deviceName;

    public DeviceSchemasId getSchemasId() {
        return new DeviceSchemasId(schemasCode);
    }
}
