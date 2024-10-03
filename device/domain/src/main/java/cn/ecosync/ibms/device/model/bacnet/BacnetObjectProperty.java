package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.device.model.DevicePointProperties;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@Getter
@ToString
public class BacnetObjectProperty implements DevicePointProperties {
    @NotNull
    private BacnetObjectType objectType;
    @NotNull
    private Integer objectInstance;
    @NotNull
    private BacnetPropertyId propertyIdentifier;
    private Integer propertyArrayIndex;

    protected BacnetObjectProperty() {
    }

    public BacnetObjectProperty(BacnetObjectType objectType, Integer objectInstance, BacnetPropertyId propertyIdentifier) {
        this(objectType, objectInstance, propertyIdentifier, null);
    }

    public BacnetObjectProperty(BacnetObjectType objectType, Integer objectInstance, BacnetPropertyId propertyIdentifier, Integer propertyArrayIndex) {
        this.objectType = objectType;
        this.objectInstance = objectInstance;
        this.propertyIdentifier = propertyIdentifier;
        this.propertyArrayIndex = propertyArrayIndex;
    }

    public BacnetObject toBacnetObject() {
        return new BacnetObject(this.objectType, this.objectInstance);
    }

    public BacnetProperty toBacnetProperty() {
        return new BacnetProperty(this.propertyIdentifier, this.propertyArrayIndex);
    }

    public Optional<Integer> getPropertyArrayIndex() {
        return Optional.ofNullable(this.propertyArrayIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BacnetObjectProperty)) return false;
        BacnetObjectProperty that = (BacnetObjectProperty) o;
        return this.objectType == that.objectType && Objects.equals(this.objectInstance, that.objectInstance) && this.propertyIdentifier == that.propertyIdentifier && Objects.equals(this.propertyArrayIndex, that.propertyArrayIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.objectType, this.objectInstance, this.propertyIdentifier, this.propertyArrayIndex);
    }
}
