package cn.ecosync.ibms.device.model.bacnet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "valueType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BacnetPropertyValue.Null.class, name = "0"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.Boolean.class, name = "1"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.UNSIGNED_INT.class, name = "2"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.SIGNED_INT.class, name = "3"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.REAL.class, name = "4"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.DOUBLE.class, name = "5"),
})
public interface BacnetPropertyValue {
    @Getter
    @ToString
    class Null implements BacnetPropertyValue {
        private Void value;
    }

    @Getter
    @ToString
    class Boolean implements BacnetPropertyValue {
        private Boolean value;
    }

    @Getter
    @ToString
    class UNSIGNED_INT implements BacnetPropertyValue {
        private Long value;
    }

    @Getter
    @ToString
    class SIGNED_INT implements BacnetPropertyValue {
        private Integer value;
    }

    @Getter
    @ToString
    class REAL implements BacnetPropertyValue {
        private Float value;
    }

    @Getter
    @ToString
    class DOUBLE implements BacnetPropertyValue {
        private Double value;
    }
}
