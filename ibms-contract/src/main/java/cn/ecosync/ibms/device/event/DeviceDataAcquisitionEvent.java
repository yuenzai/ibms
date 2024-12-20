package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.iframework.event.AbstractEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Optional;

import static cn.ecosync.ibms.Constants.AGGREGATE_TYPE_DEVICE_DAQ;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceDataAcquisitionSavedEvent.class, name = DeviceDataAcquisitionSavedEvent.EVENT_TYPE),
        @JsonSubTypes.Type(value = DeviceDataAcquisitionRemovedEvent.class, name = DeviceDataAcquisitionRemovedEvent.EVENT_TYPE),
})
public abstract class DeviceDataAcquisitionEvent extends AbstractEvent {
    public DeviceDataAcquisitionEvent() {
        super(AGGREGATE_TYPE_DEVICE_DAQ);
    }

    public abstract Optional<DeviceDataAcquisitionModel> daq();
}
