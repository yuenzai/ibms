package cn.ecosync.ibms.domain;

import cn.ecosync.ibms.dto.SchedulingTaskParams;
import cn.ecosync.iframework.serde.JsonSerde;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SchedulingTaskParamsAttributeConverter implements AttributeConverter<SchedulingTaskParams, String> {
    private final JsonSerde jsonSerde;

    public SchedulingTaskParamsAttributeConverter() {
        this(null);
    }

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
