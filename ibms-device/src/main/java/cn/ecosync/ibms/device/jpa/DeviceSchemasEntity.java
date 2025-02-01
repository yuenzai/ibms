package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.dto.DeviceSchema;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.model.IDeviceSchemas;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "device_schemas")
public class DeviceSchemasEntity extends ConcurrencySafeEntity implements IDeviceSchemas {
    @Embedded
    private DeviceSchemasId schemasId;
    @Getter
    @Convert(converter = DeviceSchemasConverter.class)
    @Column(name = "device_schemas", nullable = false)
    private DeviceSchemas deviceSchemas;

    protected DeviceSchemasEntity() {
    }

    public DeviceSchemasEntity(DeviceSchemas deviceSchemas) {
        Assert.notNull(deviceSchemas, "deviceSchemas must not be null");
        this.schemasId = deviceSchemas.getSchemasId();
        this.deviceSchemas = deviceSchemas;
    }

    public void save(DeviceSchemas deviceSchemas) {
        Assert.notNull(deviceSchemas, "deviceSchemas must not be null");
        Assert.isTrue(Objects.equals(getDeviceSchemas().getSchemasId(), deviceSchemas.getSchemasId()), "");
        Assert.isTrue(deviceSchemas.checkUniqueName(), "Schema name duplicated");
        this.deviceSchemas = deviceSchemas;
    }

    @Override
    public Collection<? extends DeviceSchema> getSchemas() {
        return getDeviceSchemas().getSchemas();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceSchemasEntity)) return false;
        DeviceSchemasEntity that = (DeviceSchemasEntity) o;
        return Objects.equals(this.schemasId, that.schemasId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(schemasId);
    }
}
