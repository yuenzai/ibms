package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@ToString
public class ReadPropertyMultipleAck {
    private Integer deviceInstance;
    private List<BacnetObjectProperties> values;

    public List<BacnetObjectProperties> getValues() {
        return CollectionUtils.nullSafeOf(values);
    }

    public boolean valuesNotEmpty() {
        return CollectionUtils.notEmpty(getValues());
    }

    public MultiValueMap<BacnetObjectProperty, BacnetPropertyValue> flatMap() {
        MultiValueMap<BacnetObjectProperty, BacnetPropertyValue> multiValueMap = new LinkedMultiValueMap<>();
        for (BacnetObjectProperties value : getValues()) {
            for (Property property : value.getProperties()) {
                BacnetObject bacnetObject = new BacnetObject(value.getObjectType(), value.getObjectInstance());
                BacnetObjectProperty key = new BacnetObjectProperty(bacnetObject, property.getProperty());
                multiValueMap.put(key, property.toValue());// toValue() return null when error exists
            }
        }
        return multiValueMap;
    }

    public static ReadPropertyMultipleAck nullInstance(Integer deviceInstance) {
        ReadPropertyMultipleAck nullInstance = new ReadPropertyMultipleAck();
        nullInstance.deviceInstance = deviceInstance;
        nullInstance.values = Collections.emptyList();
        return nullInstance;
    }

    @Getter
    @ToString
    public static class BacnetObjectProperties {
        private BacnetObjectType objectType;
        private Integer objectInstance;
        private List<Property> properties;

        public List<Property> getProperties() {
            return CollectionUtils.nullSafeOf(properties);
        }
    }

    /**
     * propertyValues 和 error 只会存在其中之一
     */
    @Getter
    @ToString
    public static class Property {
        @JsonUnwrapped
        private BacnetProperty property;
        private List<BacnetPropertyValue> propertyValues;
        private BacnetError error;

        public List<BacnetPropertyValue> getPropertyValues() {
            return CollectionUtils.nullSafeOf(propertyValues);
        }

        public Optional<BacnetError> getError() {
            return Optional.ofNullable(error);
        }

        private List<BacnetPropertyValue> toValue() {
            return error != null ? null : getPropertyValues();
        }
    }
}
