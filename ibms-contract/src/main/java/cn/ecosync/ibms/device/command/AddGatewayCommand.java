package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceGatewayId;
import lombok.ToString;

@ToString
public class AddGatewayCommand implements GatewayCommand {
    private DeviceGatewayId gatewayId;

    protected AddGatewayCommand() {
    }

    public AddGatewayCommand(DeviceGatewayId gatewayId) {
        this.gatewayId = gatewayId;
    }

    @Override
    public DeviceGatewayId gatewayId() {
        return gatewayId;
    }

    @Override
    public AddGatewayCommand withGatewayId(DeviceGatewayId gatewayId) {
        return new AddGatewayCommand(gatewayId);
    }
}
