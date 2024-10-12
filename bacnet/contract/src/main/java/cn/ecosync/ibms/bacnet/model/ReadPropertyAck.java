package cn.ecosync.ibms.bacnet.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
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
        return Optional.ofNullable(this.propertyValues).orElse(Collections.emptyList());
    }

    public Optional<BacnetError> getError() {
        return Optional.ofNullable(this.error);
    }
}
