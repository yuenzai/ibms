package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.dto.DeviceSchema;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.model.IDeviceSchemas;
import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
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
    @Column(name = "schemas", nullable = false, updatable = false)
    private DeviceSchemas schemas;

    protected DeviceSchemasEntity() {
    }

    public DeviceSchemasEntity(DeviceSchemas schemas) {
        Assert.notNull(schemas, "schemas must not be null");
        this.schemasId = schemas.getSchemasId();
        this.schemas = schemas;
    }

    public void save(DeviceSchemas schemas) {
        Assert.notNull(schemas, "schemas must not be null");
        Assert.isTrue(Objects.equals(getSchemas().getSchemasId(), schemas.getSchemasId()), "");
        Assert.isTrue(schemas.isUniqueName(), "Schema name duplicated");
        this.schemas = schemas;
    }

    @Override
    public Collection<? extends DeviceSchema> getSchemaItems() {
        return getSchemas().getSchemaItems();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceSchemasEntity)) return false;
        DeviceSchemasEntity other = (DeviceSchemasEntity) o;
        return Objects.equals(this.schemas, other.schemas);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(schemas);
    }
}
