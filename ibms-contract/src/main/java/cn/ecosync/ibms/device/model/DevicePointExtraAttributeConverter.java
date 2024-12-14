package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DevicePointExtraAttributeConverter implements AttributeConverter<DevicePointProperties, String> {
    private final JsonSerde jsonSerde;

    public DevicePointExtraAttributeConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(DevicePointProperties attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public DevicePointProperties convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, DevicePointProperties.class);
    }
}
