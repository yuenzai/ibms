package cn.ecosync.ibms.bacnet.dto;

import cn.ecosync.ibms.metrics.Measurement;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.ObservableDoubleMeasurement;
import io.opentelemetry.api.metrics.ObservableLongMeasurement;
import io.opentelemetry.api.metrics.ObservableMeasurement;

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
public interface BacnetPropertyValue extends Measurement {
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
        public void record(ObservableMeasurement observableMeasurement, Attributes attributes) {
            if (observableMeasurement instanceof ObservableLongMeasurement) {
                ObservableLongMeasurement longMeasurement = (ObservableLongMeasurement) observableMeasurement;
                longMeasurement.record(0L, attributes);
            } else if (observableMeasurement instanceof ObservableDoubleMeasurement) {
                ObservableDoubleMeasurement doubleMeasurement = (ObservableDoubleMeasurement) observableMeasurement;
                doubleMeasurement.record(0D, attributes);
            }
        }

        @Override
        public String toString() {
            return "";
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
        public void record(ObservableMeasurement observableMeasurement, Attributes attributes) {
            if (observableMeasurement instanceof ObservableLongMeasurement) {
                ObservableLongMeasurement longMeasurement = (ObservableLongMeasurement) observableMeasurement;
                longMeasurement.record(value ? 1L : 0L, attributes);
            } else if (observableMeasurement instanceof ObservableDoubleMeasurement) {
                ObservableDoubleMeasurement doubleMeasurement = (ObservableDoubleMeasurement) observableMeasurement;
                doubleMeasurement.record(value ? 1D : 0D, attributes);
            }
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
        public void record(ObservableMeasurement observableMeasurement, Attributes attributes) {
            if (observableMeasurement instanceof ObservableLongMeasurement) {
                ObservableLongMeasurement longMeasurement = (ObservableLongMeasurement) observableMeasurement;
                longMeasurement.record(value, attributes);
            } else if (observableMeasurement instanceof ObservableDoubleMeasurement) {
                ObservableDoubleMeasurement doubleMeasurement = (ObservableDoubleMeasurement) observableMeasurement;
                doubleMeasurement.record(value.doubleValue(), attributes);
            }
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
        public void record(ObservableMeasurement observableMeasurement, Attributes attributes) {
            if (observableMeasurement instanceof ObservableLongMeasurement) {
                ObservableLongMeasurement longMeasurement = (ObservableLongMeasurement) observableMeasurement;
                longMeasurement.record(value.longValue(), attributes);
            } else if (observableMeasurement instanceof ObservableDoubleMeasurement) {
                ObservableDoubleMeasurement doubleMeasurement = (ObservableDoubleMeasurement) observableMeasurement;
                doubleMeasurement.record(value.doubleValue(), attributes);
            }
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
        public void record(ObservableMeasurement observableMeasurement, Attributes attributes) {
            if (observableMeasurement instanceof ObservableLongMeasurement) {
                ObservableLongMeasurement longMeasurement = (ObservableLongMeasurement) observableMeasurement;
                longMeasurement.record(value.longValue(), attributes);
            } else if (observableMeasurement instanceof ObservableDoubleMeasurement) {
                ObservableDoubleMeasurement doubleMeasurement = (ObservableDoubleMeasurement) observableMeasurement;
                doubleMeasurement.record(value.doubleValue(), attributes);
            }
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
        public void record(ObservableMeasurement observableMeasurement, Attributes attributes) {
            if (observableMeasurement instanceof ObservableLongMeasurement) {
                ObservableLongMeasurement longMeasurement = (ObservableLongMeasurement) observableMeasurement;
                longMeasurement.record(value.longValue(), attributes);
            } else if (observableMeasurement instanceof ObservableDoubleMeasurement) {
                ObservableDoubleMeasurement doubleMeasurement = (ObservableDoubleMeasurement) observableMeasurement;
                doubleMeasurement.record(value, attributes);
            }
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    class STRING implements BacnetPropertyValue {
        private String value;

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getValueType() {
            return "99";//todo
        }

        @Override
        public void record(ObservableMeasurement observableMeasurement, Attributes attributes) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return value;
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
        public void record(ObservableMeasurement observableMeasurement, Attributes attributes) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
