//package cn.ecosync.ibms.device.model;
//
//import cn.ecosync.iframework.serde.JsonSerde;
//import cn.ecosync.iframework.serde.TypeReference;
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//import java.util.List;
//
//@Converter(autoApply = true)
//public class DevicePointsAttributeConverter implements AttributeConverter<DevicePoints, String> {
//    private final JsonSerde jsonSerde;
//
//    public DevicePointsAttributeConverter(JsonSerde jsonSerde) {
//        this.jsonSerde = jsonSerde;
//    }
//
//    @Override
//    public String convertToDatabaseColumn(DevicePoints attribute) {
//        return jsonSerde.serialize(attribute.getDevicePoints());
//    }
//
//    @Override
//    public DevicePoints convertToEntityAttribute(String dbData) {
//        List<DevicePoints.DevicePoint> devicePoints = jsonSerde.deserialize(dbData, new TypeReference<List<DevicePoints.DevicePoint>>() {
//        });
//        return new DevicePoints(devicePoints);
//    }
//}
