package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DeviceSchemasConverter implements AttributeConverter<DeviceSchemas, String> {
    private final JsonSerde jsonSerde;

    public DeviceSchemasConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(DeviceSchemas attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public DeviceSchemas convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, DeviceSchemas.class);
    }
}
