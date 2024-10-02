package cn.ecosync.ibms.jpa;

import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Collections;
import java.util.Map;

@Converter
public class MapJpaConverter implements AttributeConverter<Map<String, Object>, String> {
    private final JsonSerde jsonSerde;

    public MapJpaConverter() {
        this(null);
    }

    public MapJpaConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        return jsonSerde.writeValueAsString(attribute).orElse("");
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        return jsonSerde.readValue(dbData, new TypeReference<Map<String, Object>>() {
        }).orElse(Collections.emptyMap());
    }
}
