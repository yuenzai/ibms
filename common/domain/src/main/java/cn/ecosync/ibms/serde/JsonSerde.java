package cn.ecosync.ibms.serde;

import java.util.Optional;

public interface JsonSerde {
    Optional<String> writeValueAsString(Object value);

    <T> Optional<T> readValue(String json, Class<T> valueType);

    <T> Optional<T> readValue(String json, TypeReference<T> valueTypeRef);

    <T> Optional<T> convertValue(Object fromValue, Class<T> toValueType);

    <T> Optional<T> convertValue(Object fromValue, TypeReference<T> toValueTypeRef);
}
