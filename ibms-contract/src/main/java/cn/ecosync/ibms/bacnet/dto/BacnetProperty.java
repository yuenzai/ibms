package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.ToStringId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

@Getter
public class BacnetProperty implements ToStringId {
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

    @Override
    public String toString() {
        return toStringId();
    }

    @Override
    public String toStringId() {
        String idStr = String.valueOf(propertyIdentifier.getCode());
        if (propertyArrayIndex != null) idStr += "[" + propertyArrayIndex + "]";
        return idStr;
    }
}
