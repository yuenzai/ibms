package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.DeviceExtra;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class BacnetDeviceExtra implements DeviceExtra {
    @NotNull
    private Integer deviceInstance;
//    private String networkNumber;
//    private String macAddress;
//    private String macAddressStyle;
}
