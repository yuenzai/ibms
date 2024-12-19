package cn.ecosync.ibms.device.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

@Getter
@ToString
public class DeviceMetric {
    private Object presentValue;
    private Map<String, Object> extraValues;

    protected DeviceMetric() {
    }

    public DeviceMetric(Object presentValue) {
        this(presentValue, Collections.emptyMap());
    }

    public DeviceMetric(Object presentValue, Map<String, Object> extraValues) {
        this.presentValue = presentValue;
        this.extraValues = extraValues;
    }
}
