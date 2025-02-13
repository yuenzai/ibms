package cn.ecosync.ibms.gateway.event;

import cn.ecosync.ibms.event.AbstractEvent;

public abstract class DeviceDataAcquisitionEvent extends AbstractEvent {
    protected DeviceDataAcquisitionEvent() {
        super("GATEWAY_DATA_ACQUISITION");
    }
}
