//package cn.ecosync.ibms.device.model;
//
//import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
//import cn.ecosync.iframework.util.StringUtils;
//import jakarta.persistence.*;
//import org.apache.iceberg.Schema;
//import org.apache.iceberg.types.Types;
//import org.springframework.util.Assert;
//
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * todo 不需要这个实体类了，交给 JDBCCatalog 实现（接口还是要保留）
// */
//@Entity
//@Table(name = "device_schema")
//public class DeviceSchema extends ConcurrencySafeEntity implements DeviceSchemaCommandModel, DeviceSchemaQueryModel {
//    @Embedded
//    private DeviceSchemaId schemaId;
//    @Column(name = "description", nullable = false)
//    private String description = "";
//    @OneToMany(targetEntity = DeviceSchemaField.class, mappedBy = "schemaRef", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<DeviceSchemaField> fields = new LinkedHashSet<>();
//
//    protected DeviceSchema() {
//    }
//
//    public DeviceSchema(DeviceSchemaId schemaId, String description) {
//        Assert.notNull(schemaId, "deviceSchemaId must not be null");
//        this.schemaId = schemaId;
//        this.description = StringUtils.nullSafeOf(description);
//    }
//
//    @Override
//    public void addField(DeviceSchemaFieldModel field) {
//        DeviceSchemaField deviceSchemaField;
//        if (field instanceof DeviceSchemaField) {
//            deviceSchemaField = (DeviceSchemaField) field;
//        } else {
//            deviceSchemaField = new DeviceSchemaField(this, field.getFieldName(), field.getFieldType(), field.getOptional());
//        }
//        deviceSchemaField.schemaRef(this);
//        Assert.isTrue(fields.add(deviceSchemaField), "deviceSchema already contain the field before, should not exists");
//    }
//
//    @Override
//    public void removeField(DeviceSchemaFieldModel field) {
//        Assert.isInstanceOf(DeviceSchemaField.class, field, "field must be an instance of DeviceSchemaField");
//        DeviceSchemaField deviceSchemaField = (DeviceSchemaField) field;
//        deviceSchemaField.schemaRef(null);
//        fields.remove(deviceSchemaField);
//    }
//
//    @Override
//    public DeviceSchemaId getSchemaId() {
//        return schemaId;
//    }
//
//    @Override
//    public String getDescription() {
//        return description;
//    }
//
//    @Override
//    public Set<DeviceSchemaFieldModel> getFields() {
//        return fields.stream()
//                .map(DeviceSchemaFieldModel.class::cast)
//                .collect(Collectors.toSet());
//    }
//
//    public Schema toIcebergSchema() {
//        List<Types.NestedField> columns = fields.stream()
//                .map(DeviceSchemaField::toIcebergField)
//                .collect(Collectors.toList());
//        return new Schema(columns);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof DeviceSchema)) return false;
//        DeviceSchema that = (DeviceSchema) o;
//        return Objects.equals(schemaId, that.schemaId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(schemaId);
//    }
//}
