package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.iframework.command.Command;

public interface SaveDeviceSchemasCommand extends Command {
    DeviceSchemas getSchemas();
}
