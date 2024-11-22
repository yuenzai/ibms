package cn.ecosync.ibms.jpa.converter;

import cn.ecosync.ibms.scheduling.dto.SchedulingTrigger;
import cn.ecosync.iframework.serde.JsonSerde;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SchedulingTriggerAttributeConverter implements AttributeConverter<SchedulingTrigger, String> {
    private final JsonSerde jsonSerde;

    public SchedulingTriggerAttributeConverter() {
        this(null);
    }

    public SchedulingTriggerAttributeConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(SchedulingTrigger attribute) {
        return jsonSerde.serialize(attribute);
    }

    @Override
    public SchedulingTrigger convertToEntityAttribute(String dbData) {
        return jsonSerde.deserialize(dbData, SchedulingTrigger.class);
    }
}
