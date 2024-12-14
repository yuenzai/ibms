package cn.ecosync.ibms.bacnet.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@ToString
public class BacnetObjectProperty {
    @Valid
    @JsonUnwrapped
    private BacnetObject bacnetObject;
    @Valid
    @JsonUnwrapped
    private BacnetProperty bacnetProperty;

    protected BacnetObjectProperty() {
    }

    public BacnetObjectProperty(BacnetObject bacnetObject, BacnetProperty bacnetProperty) {
        Assert.notNull(bacnetObject, "bacnetObject must not be null");
        Assert.notNull(bacnetProperty, "bacnetProperty must not be null");
        this.bacnetObject = bacnetObject;
        this.bacnetProperty = bacnetProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BacnetObjectProperty)) return false;
        BacnetObjectProperty that = (BacnetObjectProperty) o;
        return Objects.equals(bacnetObject, that.bacnetObject) && Objects.equals(bacnetProperty, that.bacnetProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bacnetObject, bacnetProperty);
    }
}
