package cn.ecosync.ibms.serde;

import cn.ecosync.ibms.util.StringUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JacksonSerde implements JsonSerde {
    private final ObjectMapper objectMapper;

    @Override
    public Optional<String> writeValueAsString(Object value) {
        try {
            return Optional.ofNullable(objectMapper.writeValueAsString(value));
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> readValue(String json, Class<T> valueType) {
        try {
            if (!StringUtils.hasText(json)) {
                return Optional.empty();
            }
            return Optional.ofNullable(objectMapper.readValue(json, valueType));
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> readValue(String json, TypeReference<T> valueTypeRef) {
        try {
            if (!StringUtils.hasText(json)) {
                return Optional.empty();
            }
            JavaType valueType = objectMapper.getTypeFactory().constructType(valueTypeRef.getType());
            return Optional.ofNullable(objectMapper.readValue(json, valueType));
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return Optional.ofNullable(objectMapper.convertValue(fromValue, toValueType));
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        try {
            JavaType valueType = objectMapper.getTypeFactory().constructType(toValueTypeRef.getType());
            return Optional.ofNullable(objectMapper.convertValue(fromValue, valueType));
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
    }
}
