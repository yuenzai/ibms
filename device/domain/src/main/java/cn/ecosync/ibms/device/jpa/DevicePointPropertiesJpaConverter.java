package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DevicePointProperties;
import cn.ecosync.ibms.jpa.PropertiesJpaConverter;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.Converter;

@Converter
public class DevicePointPropertiesJpaConverter extends PropertiesJpaConverter<DevicePointProperties> {
    public DevicePointPropertiesJpaConverter() {
    }

    public DevicePointPropertiesJpaConverter(JsonSerde jsonSerde) {
        super(DevicePointProperties.class, jsonSerde);
    }
}
