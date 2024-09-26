package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.device.model.DevicePointValue;
import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "valueType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BacnetPropertyValue.NULL.class, name = "0"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.BOOLEAN.class, name = "1"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.UNSIGNED_INT.class, name = "2"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.SIGNED_INT.class, name = "3"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.REAL.class, name = "4"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.DOUBLE.class, name = "5"),
})
public interface BacnetPropertyValue extends DevicePointValue {
    @Getter
    @ToString
    class NULL implements BacnetPropertyValue {
        private Void value;
    }

    @Getter
    @ToString
    class BOOLEAN implements BacnetPropertyValue {
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

    @Getter
    @ToString
    class ERROR implements BacnetPropertyValue {
        private BacnetError value;

        protected ERROR() {
        }

        public ERROR(BacnetError value) {
            this.value = value;
        }
    }

    @Getter
    @ToString
    class ARRAY implements BacnetPropertyValue {
        private List<BacnetPropertyValue> value;

        protected ARRAY() {
        }

        public ARRAY(List<BacnetPropertyValue> value) {
            this.value = value;
        }

        public List<Object> getValue() {
            return CollectionUtils.nullSafeOf(this.value).stream()
                    .map(BacnetPropertyValue::getValue)
                    .collect(Collectors.toList());
        }
    }
}
