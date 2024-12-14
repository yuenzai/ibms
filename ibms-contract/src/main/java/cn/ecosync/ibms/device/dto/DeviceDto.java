package cn.ecosync.ibms.device.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DeviceDto {
    private String deviceCode;
    private String deviceName;
    private String path;
    private String description;
    private Boolean enabled;
    private DeviceExtra deviceExtra;
    private List<DevicePointDto> devicePoints;
    private DeviceStatus deviceStatus;

    protected DeviceDto() {
    }

    public DeviceDto(String deviceCode, String deviceName, String path, String description, Boolean enabled, DeviceExtra deviceExtra, List<DevicePointDto> devicePoints, DeviceStatus deviceStatus) {
        this.deviceCode = deviceCode;
        this.deviceName = deviceName;
        this.path = path;
        this.description = description;
        this.enabled = enabled;
        this.deviceExtra = deviceExtra;
        this.devicePoints = devicePoints;
        this.deviceStatus = deviceStatus;
    }

    public List<DevicePointDto> getDevicePoints() {
        return CollectionUtils.nullSafeOf(devicePoints);
    }
}
