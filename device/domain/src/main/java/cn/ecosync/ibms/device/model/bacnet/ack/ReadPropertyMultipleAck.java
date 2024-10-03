package cn.ecosync.ibms.device.model.bacnet.ack;

import cn.ecosync.ibms.device.model.bacnet.*;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@ToString
public class ReadPropertyMultipleAck {
    private BacnetObjectType objectType;
    private Integer objectInstance;
    private List<Property> properties;

    public List<Property> getProperties() {
        return CollectionUtils.nullSafeOf(this.properties);
    }

    private Stream<Map.Entry<BacnetObjectProperty, BacnetPropertyValue>> propertiesToEntry() {
        return getProperties().stream()
                .map(in -> in.toEntry(this.objectType, this.objectInstance));
    }

    /**
     * 将 ReadPropertyMultiple Ack 转换成简单的数据结构
     *
     * @param ack ReadPropertyMultiple Ack
     * @return bacnet property and it's value
     */
    public static Map<BacnetObjectProperty, BacnetPropertyValue> toSimpleMap(List<ReadPropertyMultipleAck> ack) {
        if (CollectionUtils.isEmpty(ack)) {
            return Collections.emptyMap();
        }
        return ack.stream()
                .flatMap(ReadPropertyMultipleAck::propertiesToEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));// 注意这里没有对 key 去重，如果重复会报错
    }

    @Getter
    @ToString
    public static class Property {
        private BacnetPropertyId propertyIdentifier;
        private Integer propertyArrayIndex;
        private List<BacnetPropertyValue> propertyValues;
        private BacnetError error;

        public List<BacnetPropertyValue> getPropertyValues() {
            return CollectionUtils.nullSafeOf(this.propertyValues);
        }

        public Optional<BacnetError> getError() {
            return Optional.ofNullable(this.error);
        }

        private BacnetPropertyValue toValue() {
            if (this.error != null) {
                return new BacnetPropertyValue.ERROR(this.error);
            } else {
                int valueCount = getPropertyValues().size();
                switch (valueCount) {
                    case 0:
                        return new BacnetPropertyValue.NULL();
                    case 1:
                        return CollectionUtils.firstElement(this.propertyValues);
                    default:
                        return new BacnetPropertyValue.ARRAY(this.propertyValues);
                }
            }
        }

        private Map.Entry<BacnetObjectProperty, BacnetPropertyValue> toEntry(BacnetObjectType objectType, Integer objectInstance) {
            BacnetObjectProperty key = new BacnetObjectProperty(objectType, objectInstance, getPropertyIdentifier(), getPropertyArrayIndex());
            return new AbstractMap.SimpleImmutableEntry<>(key, toValue());
        }
    }
}
