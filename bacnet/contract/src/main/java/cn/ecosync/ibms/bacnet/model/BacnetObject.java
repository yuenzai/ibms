package cn.ecosync.ibms.bacnet.model;

import lombok.Getter;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
public class BacnetObject {
    @NotNull
    private BacnetObjectType objectType;
    @NotNull
    private Integer objectInstance;

    protected BacnetObject() {
    }

    public BacnetObject(BacnetObjectType objectType, Integer objectInstance) {
        Assert.notNull(objectType, "objectType must not be null");
        Assert.notNull(objectInstance, "objectInstance must not be null");
        this.objectType = objectType;
        this.objectInstance = objectInstance;
    }

    public String getObjectTypeName() {
        return objectType.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BacnetObject)) return false;
        BacnetObject that = (BacnetObject) o;
        return objectType == that.objectType && Objects.equals(objectInstance, that.objectInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType, objectInstance);
    }

    @Override
    public String toString() {
        return objectType.getName() + "-" + objectInstance;
    }
}
