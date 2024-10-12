package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.device.model.DeviceExtra;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
public class BacnetDeviceProperties implements DeviceExtra {
    @NotNull
    private Integer deviceInstance;
    @NotBlank(message = "networkNumber must not be empty")
    private String networkNumber;
    @NotBlank(message = "macAddress must not be empty")
    private String macAddress;
    private String macAddressStyle;
}
