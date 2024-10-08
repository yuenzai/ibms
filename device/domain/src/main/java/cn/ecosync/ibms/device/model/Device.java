package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.DeviceConstant;
import cn.ecosync.ibms.device.jpa.DevicePropertiesJpaConverter;
import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.util.StringUtils;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Entity
@Table(name = "device")
public class Device extends ConcurrencySafeEntity implements AggregateRoot {
    @Embedded
    private DeviceId deviceId;
    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "dictKey", column = @Column(name = "network_id", nullable = false, updatable = false))})
    private DictionaryKey networkId;
    @Column(name = "device_name", nullable = false)
    private String deviceName;
    @Column(name = "path", nullable = false)
    private String path;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Convert(converter = DevicePropertiesJpaConverter.class)
    @Column(name = "properties", nullable = false)
    private DeviceProperties deviceProperties;

    @MapKey(name = "pointId")
    @OneToMany(targetEntity = DevicePoint.class, mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<DevicePointId, DevicePoint> devicePoints = new LinkedHashMap<>();

    protected Device() {
    }

    public Device(DeviceId deviceId, DictionaryKey networkId, String deviceName, String path, String description, DeviceProperties deviceProperties) {
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
