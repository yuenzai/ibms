//package cn.ecosync.ibms.device.model;
//
//import cn.ecosync.iframework.serde.JsonSerde;
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//@Converter(autoApply = true)
//public class DeviceExtraAttributeConverter implements AttributeConverter<DeviceExtra, String> {
//    private final JsonSerde jsonSerde;
//
//    public DeviceExtraAttributeConverter(JsonSerde jsonSerde) {
//        this.jsonSerde = jsonSerde;
//    }
//
//    @Override
//    public String convertToDatabaseColumn(DeviceExtra attribute) {
//        return jsonSerde.serialize(attribute);
//    }
//
//    @Override
//    public DeviceExtra convertToEntityAttribute(String dbData) {
//        return jsonSerde.deserialize(dbData, DeviceExtra.class);
//    }
//}
