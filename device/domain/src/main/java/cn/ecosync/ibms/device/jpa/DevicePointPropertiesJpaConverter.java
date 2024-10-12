package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DevicePointExtra;
import cn.ecosync.ibms.jpa.PropertiesJpaConverter;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.Converter;

@Converter
public class DevicePointPropertiesJpaConverter extends PropertiesJpaConverter<DevicePointExtra> {
    public DevicePointPropertiesJpaConverter() {
    }

    public DevicePointPropertiesJpaConverter(JsonSerde jsonSerde) {
        super(DevicePointExtra.class, jsonSerde);
    }
}
