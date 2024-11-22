package cn.ecosync.ibms.jpa.converter;

import cn.ecosync.ibms.device.dto.DeviceExtra;
import cn.ecosync.iframework.serde.JsonSerde;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DevicePropertiesAttributeConverter implements AttributeConverter<DeviceExtra, String> {
    private final JsonSerde jsonSerde;

    public DevicePropertiesAttributeConverter() {
        this(null);
    }

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
