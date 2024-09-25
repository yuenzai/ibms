package cn.ecosync.ibms.device.model.bacnet;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter
@ToString
public class BacnetProperty {
    @NotNull
    private BacnetPropertyId propertyIdentifier;
    private Integer propertyArrayIndex;

    public Optional<Integer> getPropertyArrayIndex() {
        return Optional.ofNullable(propertyArrayIndex);
    }
}
