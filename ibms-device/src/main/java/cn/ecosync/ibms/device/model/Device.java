package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Table(name = "DEVICE")
public class Device extends ConcurrencySafeEntity implements DeviceCommandModel {
    @Embedded
    private DeviceId deviceId;
    @Embedded
    private DeviceDataAcquisitionId daqId;
    @Embedded
    private DeviceProperties deviceProperties;

    protected Device() {
    }

    public Device(DeviceId deviceId, DeviceDataAcquisitionId daqId, DeviceProperties deviceProperties) {
        Assert.notNull(deviceId, "deviceId must not be null");
        Assert.notNull(daqId, "daqId must not be null");
        Assert.notNull(deviceProperties, "deviceProperties must not be null");
        this.deviceId = deviceId;
        this.daqId = daqId;
        this.deviceProperties = deviceProperties;
    }

    @Override
    public void update(DeviceProperties properties) {
        if (properties == null) return;
        this.deviceProperties = properties;//todo
    }

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public DeviceDataAcquisitionId getDaqId() {
        return daqId;
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

    public static Device newProbe(DeviceModel model) {
        Device probe = new Device();
        probe.deviceId = model.getDeviceId();
        probe.daqId = model.getDaqId();
        probe.deviceProperties = model.getDeviceProperties();
        return probe;
    }
}
