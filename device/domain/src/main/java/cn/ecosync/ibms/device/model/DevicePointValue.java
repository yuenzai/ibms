package cn.ecosync.ibms.device.model;

import com.fasterxml.jackson.annotation.JsonValue;

public interface DevicePointValue {
    @JsonValue
    Object getValue();
}
