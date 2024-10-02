package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceProperties;
import cn.ecosync.ibms.jpa.PropertiesJpaConverter;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.Converter;

@Converter
public class DevicePropertiesJpaConverter extends PropertiesJpaConverter<DeviceProperties> {
    public DevicePropertiesJpaConverter() {
    }

    public DevicePropertiesJpaConverter(JsonSerde jsonSerde) {
        super(DeviceProperties.class, jsonSerde);
    }
}
