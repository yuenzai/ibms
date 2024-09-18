package cn.ecosync.ibms.jpa;

import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;

import javax.persistence.AttributeConverter;
import java.util.Map;

public class ObjectJpaConverter implements AttributeConverter<Object, String> {
    private final JsonSerde jsonSerde;

    public ObjectJpaConverter() {
        this(null);
    }

    public ObjectJpaConverter(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        return jsonSerde.writeValueAsString(attribute).orElse(null);
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        return jsonSerde.readValue(dbData, new TypeReference<Map<String, Object>>() {
        }).orElse(null);
    }
}
