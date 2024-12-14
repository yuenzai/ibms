//package cn.ecosync.ibms.device.model;
//
//import lombok.ToString;
//
//import java.util.Objects;
//
//@ToString
//public class DeviceSchemaFieldDto implements DeviceSchemaFieldModel {
//    private String fieldName;
//    private String fieldType;
//    private Boolean optional;
//
//    protected DeviceSchemaFieldDto() {
//    }
//
//    public DeviceSchemaFieldDto(String fieldName, String fieldType, Boolean optional) {
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
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof DeviceSchemaFieldDto)) return false;
//        DeviceSchemaFieldDto that = (DeviceSchemaFieldDto) o;
//        return Objects.equals(fieldName, that.fieldName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(fieldName);
//    }
//}
