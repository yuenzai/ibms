package cn.ecosync.ibms.scheduling.jpa;

import cn.ecosync.ibms.jpa.PropertiesJpaConverter;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.Converter;

@Converter
public class SchedulingTriggerAttributeConverter extends PropertiesJpaConverter<SchedulingTrigger> {
    public SchedulingTriggerAttributeConverter() {
    }

    public SchedulingTriggerAttributeConverter(JsonSerde jsonSerde) {
        super(SchedulingTrigger.class, jsonSerde);
    }
}
