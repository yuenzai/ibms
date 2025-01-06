package cn.ecosync.ibms.bacnet.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;

@Getter
@ToString
public class BacnetObjectProperties {
    @Valid
    @JsonUnwrapped
    private BacnetObject bacnetObject;
    @Valid
    @NotEmpty
    private Set<BacnetProperty> properties;

    protected BacnetObjectProperties() {
    }

    public BacnetObjectProperties(BacnetObject bacnetObject, Set<BacnetProperty> properties) {
        this.bacnetObject = bacnetObject;
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BacnetObjectProperties)) return false;
        BacnetObjectProperties that = (BacnetObjectProperties) o;
        return Objects.equals(bacnetObject, that.bacnetObject);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bacnetObject);
    }
}
