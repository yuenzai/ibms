package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.DeviceConstant;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.event.AbstractEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class DeviceCreatedEvent extends AbstractEvent {
    public static final String EVENT_TYPE = "device-created";

    private final DeviceId deviceId;

    @Override
    public String aggregateType() {
        return DeviceConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return deviceId.toString();
    }

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}
