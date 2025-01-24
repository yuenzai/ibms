package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ToString
public class ReadPropertyMultipleAck {
    private Integer deviceInstance;
    private List<BacnetObjectPropertiesResult> results;

    public boolean valuesNotEmpty() {
        return CollectionUtils.notEmpty(getResults());
    }

    public Map<BacnetObject, Map<BacnetProperty, BacnetPropertyResult>> toMap() {
        return CollectionUtils.nullSafeOf(results).stream()
                .collect(Collectors.toMap(BacnetObjectPropertiesResult::toBacnetObject, BacnetObjectPropertiesResult::toPropertyMap));
    }

    public static ReadPropertyMultipleAck nullInstance(Integer deviceInstance) {
        ReadPropertyMultipleAck nullInstance = new ReadPropertyMultipleAck();
        nullInstance.deviceInstance = deviceInstance;
        nullInstance.results = Collections.emptyList();
        return nullInstance;
    }

    @Getter
    @ToString
    public static class BacnetObjectPropertiesResult {
        @NotNull
        private Integer objectType;
        @NotNull
        private Integer objectInstance;
        private List<BacnetPropertyResult> properties;

        private BacnetObject toBacnetObject() {
            return new BacnetObject(BacnetObjectType.of(objectType), objectInstance);
        }

        private Map<BacnetProperty, BacnetPropertyResult> toPropertyMap() {
            return CollectionUtils.nullSafeOf(properties).stream()
                    .collect(Collectors.toMap(BacnetPropertyResult::getProperty, Function.identity()));
        }
    }
}
