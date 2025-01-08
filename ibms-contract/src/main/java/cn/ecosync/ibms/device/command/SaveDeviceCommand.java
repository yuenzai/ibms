package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.iframework.command.Command;

public interface SaveDeviceCommand extends Command {
    DeviceId getDeviceId();

    Device toDevice(DeviceSchemasId schemasId);
}
