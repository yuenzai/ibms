package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceId;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DeviceMetricCollectedEvent {
    private String tableName;
    private Long timestamp;
    private DeviceId deviceId;

    protected DeviceMetricCollectedEvent() {
    }
}
