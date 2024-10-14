package cn.ecosync.ibms.bacnet.model;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@ToString
public class BacnetObjectProperty {
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