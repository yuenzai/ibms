package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.model.Device;
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

    public DeviceDto() {
    }

    public DeviceDto(Device device) {
        this.deviceCode = device.getDeviceId().getDeviceCode();
        this.networkId = device.getNetworkId().getDictKey();
        this.deviceName = device.getDeviceName();
        this.path = device.getPath();
        this.description = device.getDescription();
        this.enabled = device.getEnabled();
        this.deviceProperties = device.getDeviceProperties();
    }
}
