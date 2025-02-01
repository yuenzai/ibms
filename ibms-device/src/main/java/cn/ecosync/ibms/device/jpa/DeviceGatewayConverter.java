package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DeviceGatewayConverter implements AttributeConverter<DeviceGateway, String> {
    private final JsonSerde jsonSerde;

    public DeviceGatewayConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(DeviceGateway attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public DeviceGateway convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, DeviceGateway.class);
    }
}
