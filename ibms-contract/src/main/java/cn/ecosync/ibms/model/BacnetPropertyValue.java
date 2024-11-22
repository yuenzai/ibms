package cn.ecosync.ibms.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "valueType", visible = true)
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
    Object getValue();

    String getValueType();

    class NULL implements BacnetPropertyValue {
        private Void value;

        @Override
        public Void getValue() {
            return value;
        }

        @Override
        public String getValueType() {
            return "0";
        }

        @Override
        public String toString() {
            return "0";
        }
    }

    class BOOLEAN implements BacnetPropertyValue {
        private Boolean value;

        @Override
        public Boolean getValue() {
            return value;
        }

        @Override
        public String getValueType() {
            return "1";
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    class UNSIGNED_INT implements BacnetPropertyValue {
        private Long value;

        @Override
        public Long getValue() {
            return value;
        }

        @Override
        public String getValueType() {
            return "2";
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    class SIGNED_INT implements BacnetPropertyValue {
        private Integer value;

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public String getValueType() {
            return "3";
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    class REAL implements BacnetPropertyValue {
        private Float value;

        @Override
        public Float getValue() {
            return value;
        }

        @Override
        public String getValueType() {
            return "4";
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    class DOUBLE implements BacnetPropertyValue {
        private Double value;

        @Override
        public Double getValue() {
            return value;
        }

        @Override
        public String getValueType() {
            return "5";
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    class OBJECT_ID implements BacnetPropertyValue {
        private BacnetObject value;

        @Override
        public BacnetObject getValue() {
            return value;
        }

        @Override
        public String getValueType() {
            return "12";
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
