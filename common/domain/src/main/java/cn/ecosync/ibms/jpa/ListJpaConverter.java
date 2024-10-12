package cn.ecosync.ibms.jpa;

import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;

import javax.persistence.AttributeConverter;
import java.util.Collections;
import java.util.List;

public class ListJpaConverter<T> implements AttributeConverter<List<T>, String> {
    private final JsonSerde jsonSerde;

    public ListJpaConverter() {
        this(null);
    }

    public ListJpaConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        return jsonSerde.writeValueAsString(attribute).orElse("[]");
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        return jsonSerde.readValue(dbData, new TypeReference<List<T>>() {
        }).orElse(Collections.emptyList());
    }
}
