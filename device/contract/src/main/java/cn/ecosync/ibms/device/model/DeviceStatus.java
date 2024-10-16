package cn.ecosync.ibms.device.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class DeviceStatus {
    private Map<String, Object> values;
    private Long timestamp;

    protected DeviceStatus() {
    }

    public DeviceStatus(Map<String, Object> values, Long timestamp) {
        this.values = values;
        this.timestamp = timestamp;
    }
}
