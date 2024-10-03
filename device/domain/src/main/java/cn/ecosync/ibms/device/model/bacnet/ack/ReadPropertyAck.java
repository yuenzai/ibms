package cn.ecosync.ibms.device.model.bacnet.ack;

import cn.ecosync.ibms.device.model.bacnet.BacnetObjectType;
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
public class ReadPropertyAck {
    private BacnetObjectType objectType;
    private Integer objectInstance;
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
