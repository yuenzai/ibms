package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceGatewayId;
import lombok.ToString;

@ToString
public class RemoveGatewayCommand implements GatewayCommand {
    private DeviceGatewayId gatewayId;

    protected RemoveGatewayCommand() {
    }

    public RemoveGatewayCommand(DeviceGatewayId gatewayId) {
        this.gatewayId = gatewayId;
    }

    @Override
    public DeviceGatewayId gatewayId() {
        return gatewayId;
    }

    @Override
    public RemoveGatewayCommand withGatewayId(DeviceGatewayId gatewayId) {
        return new RemoveGatewayCommand(gatewayId);
    }
}
