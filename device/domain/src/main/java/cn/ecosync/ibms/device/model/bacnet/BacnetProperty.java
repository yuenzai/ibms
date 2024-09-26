package cn.ecosync.ibms.device.model.bacnet;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@Getter
@ToString
public class BacnetProperty {
    @NotNull
    private BacnetPropertyId propertyIdentifier;
    private Integer propertyArrayIndex;

    protected BacnetProperty() {
    }

    public BacnetProperty(BacnetPropertyId propertyIdentifier, Integer propertyArrayIndex) {
        this.propertyIdentifier = propertyIdentifier;
        this.propertyArrayIndex = propertyArrayIndex;
    }

    public Optional<Integer> getPropertyArrayIndex() {
        return Optional.ofNullable(propertyArrayIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BacnetProperty)) return false;
        BacnetProperty that = (BacnetProperty) o;
        return propertyIdentifier == that.propertyIdentifier && Objects.equals(propertyArrayIndex, that.propertyArrayIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyIdentifier, propertyArrayIndex);
    }
}
