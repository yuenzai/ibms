package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DeviceModel;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Optional;

@ToString
public class DeviceRemovedEvent extends AbstractDeviceEvent {
    public static final String EVENT_TYPE = "device-removed-event";

    @NotNull
    @JsonUnwrapped
    @JsonDeserialize(as = DeviceDto.class)
    private DeviceModel device;

    protected DeviceRemovedEvent() {
    }

    public DeviceRemovedEvent(DeviceModel device) {
        Assert.notNull(device, "device must not be null");
        this.device = device;
    }

    @Override
    public DeviceEventAggregator apply(DeviceEventAggregator functions) {
        functions.onApply(this);
        return functions;
    }

    @Override
    public String eventKey() {
        return device.getDeviceId().toString();
    }

    @Override
    public Optional<DeviceModel> device() {
        return Optional.ofNullable(device);
    }
}
