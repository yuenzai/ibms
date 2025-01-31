package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.iframework.command.Command;

public interface GatewayCommand extends Command {
    DeviceGatewayId gatewayId();

    GatewayCommand withGatewayId(DeviceGatewayId gatewayId);
}
