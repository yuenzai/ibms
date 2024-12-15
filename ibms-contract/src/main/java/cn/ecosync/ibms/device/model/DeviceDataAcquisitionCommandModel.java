package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.command.CreateDeviceCommand;
import cn.ecosync.iframework.event.Event;

import java.util.Collection;

public interface DeviceDataAcquisitionCommandModel extends DeviceDataAcquisitionModel {
    void update(DeviceDataAcquisitionProperties daqProperties);

    Collection<Event> createDevice(CreateDeviceCommand command);
}
