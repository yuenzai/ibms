package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.DeviceProperties;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DeviceDto {
    private String deviceCode;
    private String networkId;
    private String deviceName;
    private String path;
    private String description;
    private Boolean enabled;
    private DeviceProperties deviceProperties;
}
