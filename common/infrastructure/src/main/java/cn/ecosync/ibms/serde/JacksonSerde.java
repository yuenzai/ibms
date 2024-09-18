package cn.ecosync.ibms.serde;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
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
            return Optional.ofNullable(objectMapper.readValue(json, valueType));
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> readValue(String json, TypeReference<T> valueTypeRef) {
        try {
            JavaType valueType = objectMapper.getTypeFactory().constructType(valueTypeRef.getType());
            return Optional.ofNullable(objectMapper.readValue(json, valueType));
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
    }
}
