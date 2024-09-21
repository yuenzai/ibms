package cn.ecosync.ibms.jpa;

import cn.ecosync.ibms.serde.JsonSerde;

import javax.persistence.AttributeConverter;

public class PropertiesJpaConverter<T> implements AttributeConverter<T, String> {
    private final Class<T> type;
    private final JsonSerde jsonSerde;

    public PropertiesJpaConverter() {
        this(null, null);
    }

    public PropertiesJpaConverter(Class<T> type, JsonSerde jsonSerde) {
        this.type = type;
        this.jsonSerde = jsonSerde;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return jsonSerde.writeValueAsString(attribute).orElse(null);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return jsonSerde.readValue(dbData, type).orElse(null);
    }
}
