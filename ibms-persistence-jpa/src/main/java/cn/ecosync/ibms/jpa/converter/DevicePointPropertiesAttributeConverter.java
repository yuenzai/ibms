package cn.ecosync.ibms.jpa.converter;

import cn.ecosync.ibms.device.dto.DevicePointExtra;
import cn.ecosync.iframework.serde.JsonSerde;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DevicePointPropertiesAttributeConverter implements AttributeConverter<DevicePointExtra, String> {
    private final JsonSerde jsonSerde;

    public DevicePointPropertiesAttributeConverter() {
        this(null);
    }

    public DevicePointPropertiesAttributeConverter(JsonSerde jsonSerde) {
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
