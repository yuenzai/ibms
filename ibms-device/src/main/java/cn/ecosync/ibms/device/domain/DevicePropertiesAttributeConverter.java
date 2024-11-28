package cn.ecosync.ibms.device.domain;

import cn.ecosync.ibms.dto.DeviceExtra;
import cn.ecosync.iframework.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DevicePropertiesAttributeConverter implements AttributeConverter<DeviceExtra, String> {
    private final JsonSerde jsonSerde;

    public DevicePropertiesAttributeConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(DeviceExtra attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public DeviceExtra convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, DeviceExtra.class);
    }
}
