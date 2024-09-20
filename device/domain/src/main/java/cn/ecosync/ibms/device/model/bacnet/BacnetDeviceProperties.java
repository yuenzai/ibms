package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.device.model.DeviceProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
public class BacnetDeviceProperties implements DeviceProperties {
    @NotNull
    private String networkNumber;
    @NotNull
    private String macAddress;
    private String macAddressStyle;
}
