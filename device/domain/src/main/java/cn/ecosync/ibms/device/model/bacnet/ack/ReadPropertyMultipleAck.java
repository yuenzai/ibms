package cn.ecosync.ibms.device.model.bacnet.ack;

import cn.ecosync.ibms.device.model.bacnet.BACnetObjectType;
import cn.ecosync.ibms.device.model.bacnet.BacnetError;
import cn.ecosync.ibms.device.model.bacnet.BacnetPropertyId;
import cn.ecosync.ibms.device.model.bacnet.BacnetPropertyValue;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Getter
@ToString
public class ReadPropertyMultipleAck {
    private BACnetObjectType objectType;
    private Integer objectInstance;
    private List<Property> properties;

    public List<Property> getProperties() {
        return CollectionUtils.nullSafeOf(properties);
    }

    @Getter
    @ToString
    public static class Property {
        private BacnetPropertyId propertyIdentifier;
        private Integer propertyArrayIndex;
        private List<BacnetPropertyValue> propertyValues;
        private BacnetError error;

        public List<BacnetPropertyValue> getPropertyValues() {
            return CollectionUtils.nullSafeOf(propertyValues);
        }

        public Optional<BacnetError> getError() {
            return Optional.ofNullable(error);
        }
    }
}
