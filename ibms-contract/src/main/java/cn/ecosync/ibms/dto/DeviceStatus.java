package cn.ecosync.ibms.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class DeviceStatus {
    private String deviceCode;
    private Map<String, Object> values;
    private Long timestamp;

    protected DeviceStatus() {
    }

    public DeviceStatus(String deviceCode, Map<String, Object> values, Long timestamp) {
        this.deviceCode = deviceCode;
        this.values = values;
        this.timestamp = timestamp;
    }
}
