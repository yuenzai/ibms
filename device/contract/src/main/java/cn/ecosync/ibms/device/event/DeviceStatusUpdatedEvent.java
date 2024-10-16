package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.DeviceConstant;
import cn.ecosync.ibms.device.model.DeviceStatus;
import cn.ecosync.ibms.event.AbstractEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class DeviceStatusUpdatedEvent extends AbstractEvent {
    private final String deviceCode;
    private final DeviceStatus deviceStatus;

    @Override
    public String aggregateType() {
        return DeviceConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return deviceCode;
    }

    @Override
    public String eventType() {
        return "device-status-updated";
    }
}
