package cn.ecosync.ibms.scheduling.jpa;

import cn.ecosync.ibms.jpa.PropertiesJpaConverter;
import cn.ecosync.ibms.scheduling.model.SchedulingTask;
import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.Converter;

@Converter
public class SchedulingTaskAttributeConverter extends PropertiesJpaConverter<SchedulingTask> {
    public SchedulingTaskAttributeConverter() {
    }

    public SchedulingTaskAttributeConverter(JsonSerde jsonSerde) {
        super(SchedulingTask.class, jsonSerde);
    }
}
