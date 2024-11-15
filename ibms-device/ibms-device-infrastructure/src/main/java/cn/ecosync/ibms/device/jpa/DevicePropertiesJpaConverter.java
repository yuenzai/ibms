package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceExtra;
import cn.ecosync.ibms.jpa.PropertiesJpaConverter;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class DevicePropertiesJpaConverter extends PropertiesJpaConverter<DeviceExtra> {
    public DevicePropertiesJpaConverter() {
    }

    public DevicePropertiesJpaConverter(JsonSerde jsonSerde) {
        super(DeviceExtra.class, jsonSerde);
    }
}
