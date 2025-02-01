package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.opentelemetry.api.common.AttributesBuilder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.function.Consumer;

@Getter
@ToString
public class BacnetPropertyResult implements Consumer<AttributesBuilder> {
    @JsonUnwrapped
    private BacnetProperty property;
    private BacnetPropertyValue value;
    private List<BacnetPropertyValue> values;
    private BacnetError error;

    @Override
    public void accept(AttributesBuilder attributesBuilder) {
        if (error != null || BacnetProperty.PROPERTY_PRESENT_VALUE.equals(property)) return;
        String attributeName = property.getPropertyIdentifier().getName();
        if (value instanceof BacnetPropertyValue.BOOLEAN) {
            attributesBuilder.put(attributeName, ((BacnetPropertyValue.BOOLEAN) value).getValue());
        } else if (value instanceof BacnetPropertyValue.UNSIGNED_INT) {
            attributesBuilder.put(attributeName, ((BacnetPropertyValue.UNSIGNED_INT) value).getValue());
        } else if (value instanceof BacnetPropertyValue.SIGNED_INT) {
            attributesBuilder.put(attributeName, ((BacnetPropertyValue.SIGNED_INT) value).getValue());
        } else if (value instanceof BacnetPropertyValue.REAL) {
            attributesBuilder.put(attributeName, ((BacnetPropertyValue.REAL) value).getValue());
        } else if (value instanceof BacnetPropertyValue.DOUBLE) {
            attributesBuilder.put(attributeName, ((BacnetPropertyValue.DOUBLE) value).getValue());
        } else if (value instanceof BacnetPropertyValue.STRING) {
            attributesBuilder.put(attributeName, ((BacnetPropertyValue.STRING) value).getValue());
        }
    }

    public List<BacnetPropertyValue> getValues() {
        return CollectionUtils.nullSafeOf(values);
    }
}
