package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import cn.ecosync.iframework.util.StringUtils;
import jakarta.persistence.Column;
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
    private DeviceDataAcquisitionId daqId;
    @Column(name = "device_name", nullable = false)
    private String deviceName;
    @Column(name = "path", nullable = false)
    private String path;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "device_extra", nullable = false)
    private DeviceExtra deviceExtra;

    protected Device() {
    }

    public Device(DeviceId deviceId, DeviceDataAcquisitionId daqId, String deviceName, String path, String description, DeviceExtra deviceExtra) {
        Assert.notNull(deviceId, "deviceId can not be null");
        Assert.notNull(daqId, "daqId can not be null");
        Assert.notNull(deviceExtra, "deviceExtra can not be null");
        this.deviceId = deviceId;
        this.daqId = daqId;
        this.deviceName = StringUtils.nullSafeOf(deviceName);
        this.path = StringUtils.nullSafeOf(path);
        this.description = StringUtils.nullSafeOf(description);
        this.deviceExtra = deviceExtra;
    }

    @Override
    public void update(String deviceName, String path, String description, DeviceExtra deviceExtra) {
        if (deviceName != null) this.deviceName = deviceName;
        if (path != null) this.path = path;
        if (description != null) this.description = description;
        if (deviceExtra != null) this.deviceExtra = deviceExtra;
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
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public DeviceExtra getDeviceExtra() {
        return deviceExtra;
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

    public static Device newProbe(DeviceDataAcquisitionId daqId, String deviceName, String path) {
        Device probe = new Device();
        probe.daqId = daqId;
        probe.deviceName = deviceName;
        probe.path = path;
        return probe;
    }
}
