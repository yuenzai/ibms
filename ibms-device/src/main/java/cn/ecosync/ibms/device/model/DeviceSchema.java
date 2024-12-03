package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Table(name = "device_schema")
public class DeviceSchema extends ConcurrencySafeEntity implements DeviceSchemaCommandModel, DeviceSchemaQueryModel {
    @Valid
    @NotNull
    @Embedded
    private DeviceSchemaId deviceSchemaId;
    @Valid
    @NotNull
    @Column(name = "device_points", nullable = false)
    private DevicePoints devicePoints;

    protected DeviceSchema() {
    }

    public DeviceSchema(DeviceSchemaId deviceSchemaId, DevicePoints devicePoints) {
        Assert.notNull(deviceSchemaId, "deviceSchemaId can not be null");
        Assert.notNull(devicePoints, "devicePoints can not be null");
        this.deviceSchemaId = deviceSchemaId;
        this.devicePoints = devicePoints;
    }

    @Override
    public DeviceSchemaId getDeviceSchemaId() {
        return deviceSchemaId;
    }

    @Override
    public DevicePoints getDevicePoints() {
        return devicePoints;
    }

    @Override
    public Schema toAvroSchema() {
        SchemaBuilder.FieldAssembler<Schema> fieldAssembler = SchemaBuilder.record(deviceSchemaId.getSubjectName()).fields();
        for (DevicePoints.DevicePoint devicePoint : devicePoints) {
            String name = devicePoint.getPointCode();
            String defaultValue = devicePoint.getDefaultValue().orElse(null);
            switch (devicePoint.getPointType()) {
                case INT:
                    fieldAssembler = defaultValue != null
                            ? fieldAssembler.nullableInt(name, Integer.parseInt(defaultValue))
                            : fieldAssembler.optionalInt(name);
                case LONG:
                    fieldAssembler = defaultValue != null
                            ? fieldAssembler.nullableLong(name, Long.parseLong(defaultValue))
                            : fieldAssembler.optionalLong(name);
                case FLOAT:
                    fieldAssembler = defaultValue != null
                            ? fieldAssembler.nullableFloat(name, Float.parseFloat(defaultValue))
                            : fieldAssembler.optionalFloat(name);
                case DOUBLE:
                    fieldAssembler = defaultValue != null
                            ? fieldAssembler.nullableDouble(name, Double.parseDouble(defaultValue))
                            : fieldAssembler.optionalDouble(name);
                case BOOLEAN:
                    fieldAssembler = defaultValue != null
                            ? fieldAssembler.nullableBoolean(name, Boolean.parseBoolean(defaultValue))
                            : fieldAssembler.optionalBoolean(name);
            }
        }
        return fieldAssembler.endRecord();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceSchema)) return false;
        DeviceSchema that = (DeviceSchema) o;
        return Objects.equals(deviceSchemaId, that.deviceSchemaId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceSchemaId);
    }
}
