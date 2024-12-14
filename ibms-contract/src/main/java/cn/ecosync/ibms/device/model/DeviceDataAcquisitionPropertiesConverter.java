package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DeviceDataAcquisitionPropertiesConverter implements AttributeConverter<DeviceDataAcquisitionProperties, String> {
    private final JsonSerde jsonSerde;

    public DeviceDataAcquisitionPropertiesConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(DeviceDataAcquisitionProperties attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public DeviceDataAcquisitionProperties convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, DeviceDataAcquisitionProperties.class);
    }
}
