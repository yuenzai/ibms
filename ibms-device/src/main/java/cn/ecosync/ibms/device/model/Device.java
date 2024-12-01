package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.Constants;
import cn.ecosync.iframework.domain.AggregateRoot;
import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import cn.ecosync.iframework.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Table(name = "device")
public class Device extends ConcurrencySafeEntity implements DeviceCommandModel, DeviceQueryModel, AggregateRoot {
    @Valid
    @NotNull
    @Embedded
    private DeviceId deviceId;
    @Valid
    @NotNull
    @Embedded
    private DeviceSchemaId deviceSchemaId;
    @Column(name = "device_name", nullable = false)
    private String deviceName = "";
    @Column(name = "path", nullable = false)
    private String path = "";
    @Column(name = "description", nullable = false)
    private String description = "";
    @Valid
    @NotNull
    @Column(name = "device_extra", nullable = false)
    private DeviceExtra deviceExtra;

    protected Device() {
    }

    public Device(DeviceId deviceId, DeviceSchemaId deviceSchemaId, String deviceName, String path, String description, DeviceExtra deviceExtra) {
        Assert.notNull(deviceId, "deviceId can not be null");
        Assert.notNull(deviceSchemaId, "deviceSchemaId can not be null");
        Assert.notNull(deviceExtra, "deviceExtra can not be null");
        this.deviceId = deviceId;
        this.deviceSchemaId = deviceSchemaId;
        this.deviceName = StringUtils.nullSafeOf(deviceName);
        this.path = StringUtils.nullSafeOf(path);
        this.description = StringUtils.nullSafeOf(description);
        this.deviceExtra = deviceExtra;
    }

    @Override
    public String aggregateType() {
        return Constants.AGGREGATE_TYPE_DEVICE;
    }

    @Override
    public String aggregateId() {
        return deviceId.toString();
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

    @Override
    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public DeviceSchemaId getDeviceSchemaId() {
        return deviceSchemaId;
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
}
