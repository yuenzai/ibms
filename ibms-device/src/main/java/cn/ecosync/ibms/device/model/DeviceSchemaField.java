//package cn.ecosync.ibms.device.model;
//
//import cn.ecosync.iframework.domain.IdentifiedValueObject;
//import jakarta.persistence.*;
//import org.apache.iceberg.types.Type;
//import org.apache.iceberg.types.Types;
//import org.springframework.util.Assert;
//
//import java.util.Objects;
//
///**
// * todo 不需要这个实体类了，交给 JDBCCatalog 实现（接口还是要保留）
// */
//@Entity
//@Table(name = "device_schema_field")
//public class DeviceSchemaField extends IdentifiedValueObject implements DeviceSchemaFieldModel {
//    @ManyToOne(targetEntity = DeviceSchema.class)
//    @JoinColumn(name = "schema_id", nullable = false, updatable = false)
//    private DeviceSchema schemaRef;
//    @Column(name = "field_name", nullable = false, updatable = false)
//    private String fieldName;
//    @Column(name = "field_type", nullable = false, updatable = false)
//    private String fieldType;
//    @Column(name = "is_optional", nullable = false, updatable = false)
//    private Boolean optional;
//
//    protected DeviceSchemaField() {
//    }
//
//    public DeviceSchemaField(DeviceSchema schemaRef, String fieldName, String fieldType, Boolean optional) {
//        Assert.notNull(schemaRef, "schemaRef must not be null");
//        Assert.hasText(fieldName, "fieldName must not be empty");
//        toIcebergType(fieldType);
//        Assert.notNull(optional, "optional must not be null");
//        this.schemaRef = schemaRef;
//        this.fieldName = fieldName;
//        this.fieldType = fieldType;
//        this.optional = optional;
//    }
//
//    @Override
//    public String getFieldName() {
//        return fieldName;
//    }
//
//    @Override
//    public String getFieldType() {
//        return fieldType;
//    }
//
//    @Override
//    public Boolean getOptional() {
//        return optional;
//    }
//
//    public void schemaRef(DeviceSchema schemaRef) {
//        this.schemaRef = schemaRef;
//    }
//
//    public Types.NestedField toIcebergField() {
//        return Types.NestedField.of(id, optional, fieldName, toIcebergType(fieldType));
//    }
//
//    private Type.PrimitiveType toIcebergType(String fieldType) {
//        return Types.fromPrimitiveString(fieldType);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof DeviceSchemaField)) return false;
//        DeviceSchemaField that = (DeviceSchemaField) o;
//        return Objects.equals(fieldName, that.fieldName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(fieldName);
//    }
//}
