package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceDataPoint;
import cn.ecosync.ibms.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DeviceDataPointConverter implements AttributeConverter<DeviceDataPoint, String> {
    private final JsonSerde jsonSerde;

    public DeviceDataPointConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(DeviceDataPoint attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public DeviceDataPoint convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, DeviceDataPoint.class);
    }
}
