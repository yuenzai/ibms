package cn.ecosync.ibms.bacnet.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.util.Assert;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BacnetObject)) return false;
        BacnetObject that = (BacnetObject) o;
        return this.objectType == that.objectType && Objects.equals(this.objectInstance, that.objectInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType, objectInstance);
    }

    @Override
    public String toString() {
        return objectType.toString() + "_" + objectInstance;
    }
}
