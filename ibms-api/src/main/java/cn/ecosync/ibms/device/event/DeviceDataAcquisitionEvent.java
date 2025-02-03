package cn.ecosync.ibms.device.event;

import cn.ecosync.ibms.event.AbstractEvent;

public abstract class DeviceDataAcquisitionEvent extends AbstractEvent {
    protected DeviceDataAcquisitionEvent() {
        super("DEVICE_DATA_ACQUISITION");
    }
}
