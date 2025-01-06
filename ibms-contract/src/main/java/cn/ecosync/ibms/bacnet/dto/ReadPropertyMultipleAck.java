package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
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
                .collect(Collectors.toMap(BacnetObjectPropertiesResult::getBacnetObject, BacnetObjectPropertiesResult::toPropertyMap));
    }

    public static ReadPropertyMultipleAck nullInstance(Integer deviceInstance) {
        ReadPropertyMultipleAck nullInstance = new ReadPropertyMultipleAck();
        nullInstance.deviceInstance = deviceInstance;
        nullInstance.results = Collections.emptyList();
        return nullInstance;
    }

    @ToString
    public static class BacnetObjectPropertiesResult {
        @JsonUnwrapped
        private BacnetObject bacnetObject;
        private List<BacnetPropertyResult> properties;

        private BacnetObject getBacnetObject() {
            return bacnetObject;
        }

        private Map<BacnetProperty, BacnetPropertyResult> toPropertyMap() {
            return CollectionUtils.nullSafeOf(properties).stream()
                    .collect(Collectors.toMap(BacnetPropertyResult::getProperty, Function.identity()));
        }
    }
}
