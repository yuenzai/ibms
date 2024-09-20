package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DevicePointProperties;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.AttributeConverter;

public class DevicePointPropertiesJpaConverter implements AttributeConverter<DevicePointProperties, String> {
    private final JsonSerde jsonSerde;

    public DevicePointPropertiesJpaConverter() {
        this(null);
    }

    public DevicePointPropertiesJpaConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(DevicePointProperties attribute) {
        return jsonSerde.writeValueAsString(attribute).orElse(null);
    }

    @Override
    public DevicePointProperties convertToEntityAttribute(String dbData) {
        return jsonSerde.readValue(dbData, DevicePointProperties.class).orElse(null);
    }
}
