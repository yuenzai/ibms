package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.iframework.command.Command;

public interface SaveDeviceCommand extends Command {
    Device getDevice();
}
