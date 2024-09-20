package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceProperties;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.AttributeConverter;

public class DevicePropertiesJpaConverter implements AttributeConverter<DeviceProperties, String> {
    private final JsonSerde jsonSerde;

    public DevicePropertiesJpaConverter() {
        this(null);
    }

    public DevicePropertiesJpaConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(DeviceProperties attribute) {
        return jsonSerde.writeValueAsString(attribute).orElse(null);
    }

    @Override
    public DeviceProperties convertToEntityAttribute(String dbData) {
        return jsonSerde.readValue(dbData, DeviceProperties.class).orElse(null);
    }
}
