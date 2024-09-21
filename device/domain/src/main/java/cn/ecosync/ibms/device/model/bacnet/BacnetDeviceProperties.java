package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.device.model.DeviceConstant;
import cn.ecosync.ibms.device.model.DeviceProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@EqualsAndHashCode
public class BacnetDeviceProperties implements DeviceProperties {
    @NotBlank(message = "networkNumber must not be empty")
    private String networkNumber;
    @NotBlank(message = "macAddress must not be empty")
    private String macAddress;
    private String macAddressStyle;

    @Override
    public String type() {
        return DeviceConstant.BACNET;
    }
}
