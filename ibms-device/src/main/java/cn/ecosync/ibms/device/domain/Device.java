package cn.ecosync.ibms.device.domain;

import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "device")
public class Device extends ConcurrencySafeEntity {
    @Embedded
    private DeviceId deviceId;

    @Embedded
    private DeviceProperties deviceProperties;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @MapKey(name = "pointId")
    @OneToMany(targetEntity = DevicePoint.class, mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<DevicePointId, DevicePoint> devicePoints = new LinkedHashMap<>();

    protected Device() {
    }

    public Device(DeviceId deviceId, DeviceProperties deviceProperties) {
        Assert.notNull(deviceId, "deviceId can not be null");
        Assert.notNull(deviceProperties, "deviceProperties can not be null");
        this.deviceId = deviceId;
        this.deviceProperties = deviceProperties;
        this.enabled = Boolean.TRUE;
    }

    public void update(DeviceProperties deviceProperties) {
        if (deviceProperties != null) {
            this.deviceProperties = deviceProperties;
        }
    }

    public void enable() {
        this.enabled = Boolean.TRUE;
    }

    public void disable() {
        this.enabled = Boolean.FALSE;
    }

    public DeviceId deviceId() {
        return deviceId;
    }

    public DeviceProperties deviceProperties() {
        return deviceProperties;
    }

    public Boolean enabled() {
        return enabled;
    }

    public Map<DevicePointId, DevicePoint> devicePoints() {
        return devicePoints;
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
