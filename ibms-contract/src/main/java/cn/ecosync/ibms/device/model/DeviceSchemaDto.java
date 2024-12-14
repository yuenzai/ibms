//package cn.ecosync.ibms.device.model;
//
//import cn.ecosync.iframework.util.CollectionUtils;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import lombok.ToString;
//import org.springframework.util.Assert;
//
//import java.util.LinkedHashSet;
//import java.util.Objects;
//import java.util.Set;
//
//@ToString
//public class DeviceSchemaDto implements DeviceSchemaQueryModel {
//    private DeviceSchemaId schemaId;
//    private String description;
//    @JsonDeserialize(as = LinkedHashSet.class, contentAs = DeviceSchemaFieldDto.class)
//    private Set<DeviceSchemaFieldModel> fields;
//
//    protected DeviceSchemaDto() {
//    }
//
//    public DeviceSchemaDto(DeviceSchemaId schemaId, String description, Set<DeviceSchemaFieldModel> fields) {
//        Assert.notNull(schemaId, "deviceSchemaId must not be null");
//        this.schemaId = schemaId;
//        this.description = description;
//        this.fields = CollectionUtils.nullSafeOf(fields);
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
//        return fields;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof DeviceSchemaDto)) return false;
//        DeviceSchemaDto that = (DeviceSchemaDto) o;
//        return Objects.equals(schemaId, that.schemaId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(schemaId);
//    }
//}
