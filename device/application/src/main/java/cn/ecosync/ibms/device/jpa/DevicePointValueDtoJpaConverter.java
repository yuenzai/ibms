package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DevicePointValueDto;
import cn.ecosync.ibms.jpa.ListJpaConverter;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class DevicePointValueDtoJpaConverter extends ListJpaConverter<DevicePointValueDto> {
    public DevicePointValueDtoJpaConverter() {
    }

    public DevicePointValueDtoJpaConverter(JsonSerde jsonSerde) {
        super(jsonSerde);
    }
}
