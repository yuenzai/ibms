package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
public class BacnetObjectProperties {
    @Valid
    @JsonUnwrapped
    private BacnetObject bacnetObject;
    @NotEmpty
    private List<@Valid BacnetProperty> properties;

    protected BacnetObjectProperties() {
    }

    public BacnetObjectProperties(BacnetDataPoint bacnetDataPoint) {
        this(bacnetDataPoint.getBacnetObject());
    }

    public BacnetObjectProperties(BacnetObject bacnetObject) {
        this(bacnetObject, BacnetProperty.PROPERTY_PRESENT_VALUE);
    }

    public BacnetObjectProperties(BacnetObject bacnetObject, BacnetProperty... properties) {
        Assert.notNull(bacnetObject, "bacnetObject must not be null");
        Assert.notEmpty(properties, "properties must not be null");
        this.bacnetObject = bacnetObject;
        this.properties = Arrays.asList(properties);
    }

    public List<BacnetProperty> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BacnetObjectProperties)) return false;
        BacnetObjectProperties that = (BacnetObjectProperties) o;
        return Objects.equals(this.bacnetObject, that.bacnetObject);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bacnetObject);
    }
}
