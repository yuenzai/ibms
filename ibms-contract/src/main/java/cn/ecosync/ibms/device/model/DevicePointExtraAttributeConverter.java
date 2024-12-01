package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DevicePointExtraAttributeConverter implements AttributeConverter<DevicePointExtra, String> {
    private final JsonSerde jsonSerde;

    public DevicePointExtraAttributeConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(DevicePointExtra attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public DevicePointExtra convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, DevicePointExtra.class);
    }
}
