package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.iframework.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DeviceConverter implements AttributeConverter<Device, String> {
    private final JsonSerde jsonSerde;

    public DeviceConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(Device attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public Device convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, Device.class);
    }
}
