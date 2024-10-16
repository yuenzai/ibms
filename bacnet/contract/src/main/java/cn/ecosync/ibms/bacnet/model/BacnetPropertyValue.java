package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "valueType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BacnetPropertyValue.NULL.class, name = "0"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.BOOLEAN.class, name = "1"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.UNSIGNED_INT.class, name = "2"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.SIGNED_INT.class, name = "3"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.REAL.class, name = "4"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.DOUBLE.class, name = "5"),
        @JsonSubTypes.Type(value = BacnetPropertyValue.OBJECT_ID.class, name = "12"),
})
public interface BacnetPropertyValue {
    Object toObject();

    @Getter
    @ToString
    class NULL implements BacnetPropertyValue {
        private Void value;

        @Override
        public Void toObject() {
            return value;
        }
    }

    @Getter
    @ToString
    class BOOLEAN implements BacnetPropertyValue {
        private Boolean value;

        @Override
        public Boolean toObject() {
            return value;
        }
    }

    @Getter
    @ToString
    class UNSIGNED_INT implements BacnetPropertyValue {
        private Long value;

        @Override
        public Long toObject() {
            return value;
        }
    }

    @Getter
    @ToString
    class SIGNED_INT implements BacnetPropertyValue {
        private Integer value;

        @Override
        public Integer toObject() {
            return value;
        }
    }

    @Getter
    @ToString
    class REAL implements BacnetPropertyValue {
        private Float value;

        @Override
        public Float toObject() {
            return value;
        }
    }

    @Getter
    @ToString
    class DOUBLE implements BacnetPropertyValue {
        private Double value;

        @Override
        public Double toObject() {
            return value;
        }
    }

    @Getter
    @ToString
    class OBJECT_ID implements BacnetPropertyValue {
        private BacnetObject value;

        @Override
        public String toObject() {
            return Optional.ofNullable(value)
                    .map(BacnetObject::toString)
                    .orElse(null);
        }
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

        @Override
        public BacnetError toObject() {
            return value;
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

        @Override
        public List<Object> toObject() {
            return CollectionUtils.nullSafeOf(value).stream()
                    .map(BacnetPropertyValue::toObject)
                    .collect(Collectors.toList());
        }
    }
}
