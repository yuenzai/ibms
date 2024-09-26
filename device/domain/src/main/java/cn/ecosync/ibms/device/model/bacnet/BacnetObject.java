package cn.ecosync.ibms.device.model.bacnet;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@ToString
public class BacnetObject {
    @NotNull
    private BACnetObjectType objectType;
    @NotNull
    private Integer objectInstance;

    protected BacnetObject() {
    }

    public BacnetObject(BACnetObjectType objectType, Integer objectInstance) {
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
        return objectType == that.objectType && Objects.equals(objectInstance, that.objectInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType, objectInstance);
    }
}
