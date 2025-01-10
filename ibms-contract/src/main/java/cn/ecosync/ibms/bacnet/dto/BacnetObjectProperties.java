package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
public class BacnetObjectProperties {
    @Valid
    @JsonUnwrapped
    private BacnetObject bacnetObject;
    @NotEmpty
    private List<@Pattern(regexp = BacnetProperty.REGEXP, message = "REGEXP not match") String> properties;

    protected BacnetObjectProperties() {
    }

    public BacnetObjectProperties(BacnetObject bacnetObject, List<String> properties) {
        this.bacnetObject = bacnetObject;
        this.properties = properties;
    }

    public Set<BacnetProperty> toProperties() {
        return CollectionUtils.nullSafeOf(properties).stream()
                .map(BacnetProperty::fromString)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
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
