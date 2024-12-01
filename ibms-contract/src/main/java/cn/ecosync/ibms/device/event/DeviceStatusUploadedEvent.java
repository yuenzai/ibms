package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceId;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DeviceStatusUploadedEvent {
    private DeviceId deviceId;
    private Long timestamp;

    protected DeviceStatusUploadedEvent() {
    }
}
