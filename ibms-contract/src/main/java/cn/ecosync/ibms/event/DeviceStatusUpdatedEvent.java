package cn.ecosync.ibms.event;

import cn.ecosync.ibms.DeviceConstant;
import cn.ecosync.ibms.dto.DeviceStatus;
import cn.ecosync.iframework.event.AbstractEvent;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class DeviceStatusUpdatedEvent extends AbstractEvent {
    @JsonUnwrapped
    private final DeviceStatus deviceStatus;

    public DeviceStatusUpdatedEvent(DeviceStatus deviceStatus) {
        Assert.notNull(deviceStatus, "deviceStatus can not be null");
        this.deviceStatus = deviceStatus;
    }

    @Override
    public String aggregateType() {
        return DeviceConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return deviceStatus.getDeviceCode();
    }

    @Override
    public String eventType() {
        return "device-status-updated";
    }
}
