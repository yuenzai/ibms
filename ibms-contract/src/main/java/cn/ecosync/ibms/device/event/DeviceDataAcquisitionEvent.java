package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.iframework.event.AbstractEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Optional;

import static cn.ecosync.ibms.Constants.AGGREGATE_TYPE_DEVICE_DAQ;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType", visible = true)
@JsonSubTypes({
})
public abstract class DeviceDataAcquisitionEvent extends AbstractEvent {
    public DeviceDataAcquisitionEvent() {
        super(AGGREGATE_TYPE_DEVICE_DAQ);
    }

    public abstract Optional<DeviceDataAcquisitionModel> daq();
}
