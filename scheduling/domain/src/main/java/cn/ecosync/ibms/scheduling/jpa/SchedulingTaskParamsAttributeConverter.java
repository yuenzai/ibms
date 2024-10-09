package cn.ecosync.ibms.scheduling.jpa;

import cn.ecosync.ibms.jpa.PropertiesJpaConverter;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.Converter;

@Converter
public class SchedulingTaskParamsAttributeConverter extends PropertiesJpaConverter<SchedulingTaskParams> {
    public SchedulingTaskParamsAttributeConverter() {
    }

    public SchedulingTaskParamsAttributeConverter(JsonSerde jsonSerde) {
        super(SchedulingTaskParams.class, jsonSerde);
    }
}
