package cn.ecosync.ibms.scheduling.domain;

import cn.ecosync.ibms.scheduling.dto.SchedulingTaskParams;
import cn.ecosync.iframework.serde.JsonSerde;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SchedulingTaskParamsAttributeConverter implements AttributeConverter<SchedulingTaskParams, String> {
    private final JsonSerde jsonSerde;

    public SchedulingTaskParamsAttributeConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(SchedulingTaskParams attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public SchedulingTaskParams convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, SchedulingTaskParams.class);
    }
}
