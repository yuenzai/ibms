package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Table(name = "device")
public class Device extends ConcurrencySafeEntity implements DeviceCommandModel {
    @Embedded
    private DeviceId deviceId;
    @Embedded
    private DeviceProperties deviceProperties;

    protected Device() {
    }

    public Device(DeviceId deviceId, DeviceProperties deviceProperties) {
        Assert.notNull(deviceId, "deviceId must be be null");
        Assert.notNull(deviceProperties, "deviceProperties must be be null");
        this.deviceId = deviceId;
        this.deviceProperties = deviceProperties;
    }

    @Override
    public void update(DeviceProperties properties) {
        if (properties == null) return;
        this.deviceProperties = properties;
    }

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public DeviceProperties getDeviceProperties() {
        return deviceProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Device)) return false;
        Device device = (Device) o;
        return Objects.equals(deviceId, device.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceId);
    }

    public static Device newProbe(DeviceId deviceIdProbe, DeviceProperties devicePropertiesProbe) {
        Device probe = new Device();
        probe.deviceId = deviceIdProbe;
        probe.deviceProperties = devicePropertiesProbe;
        return probe;
    }
}
