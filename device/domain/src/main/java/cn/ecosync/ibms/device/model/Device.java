package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import cn.ecosync.ibms.system.model.SystemDictionaryKey;
import cn.ecosync.ibms.util.StringUtils;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class Device extends ConcurrencySafeEntity implements AggregateRoot {
    private DeviceId deviceId;
    private SystemDictionaryKey networkId;
    private String deviceName;
    private String path;
    private String description;
    private DeviceProperties deviceProperties;
    private Map<DevicePointId, DevicePoint> devicePoints = new LinkedHashMap<>();
    private Boolean enabled;

    protected Device() {
    }

    public Device(DeviceId deviceId, SystemDictionaryKey networkId, String deviceName, String path, String description, DeviceProperties deviceProperties) {
        Assert.notNull(deviceId, "device id must not be null");
        Assert.notNull(networkId, "network id must not be null");
        Assert.notNull(deviceProperties, "device properties must not be null");
        this.deviceId = deviceId;
        this.networkId = networkId;
        this.deviceName = StringUtils.nullSafeOf(deviceName);
        this.path = StringUtils.nullSafeOf(path);
        this.description = StringUtils.nullSafeOf(description);
        this.deviceProperties = deviceProperties;
        this.enabled = Boolean.TRUE;
    }

    public void setDeviceName(String deviceName) {
        if (deviceName != null) {
            this.deviceName = deviceName;
        }
    }

    public void setPath(String path) {
        if (path != null) {
            this.path = path;
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void setDeviceProperties(DeviceProperties deviceProperties) {
        if (deviceProperties != null) {
            this.deviceProperties = deviceProperties;
        }
    }

    public void setEnabled(Boolean enabled) {
        if (enabled != null) {
            this.enabled = enabled;
        }
    }

    @Override
    public String aggregateType() {
        return DeviceConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return deviceId.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        return Objects.equals(deviceId, device.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceId);
    }
}
