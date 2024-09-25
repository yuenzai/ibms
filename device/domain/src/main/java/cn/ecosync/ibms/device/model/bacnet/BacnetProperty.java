package cn.ecosync.ibms.device.model.bacnet;

import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@ToString
public class BacnetProperty {
    private BacnetPropertyId propertyIdentifier;
    private Integer propertyArrayIndex;

    public Optional<Integer> getPropertyArrayIndex() {
        return Optional.ofNullable(propertyArrayIndex);
    }
}
