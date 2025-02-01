package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum;
import lombok.ToString;

@ToString
public class SetGatewaySynchronizationStateCommand implements GatewayCommand {
    private DeviceGatewayId gatewayId;
    private SynchronizationStateEnum synchronizationState;

    protected SetGatewaySynchronizationStateCommand() {
    }

    public SetGatewaySynchronizationStateCommand(SynchronizationStateEnum synchronizationState) {
        this(null, synchronizationState);
    }

    private SetGatewaySynchronizationStateCommand(DeviceGatewayId gatewayId, SynchronizationStateEnum synchronizationState) {
        this.gatewayId = gatewayId;
        this.synchronizationState = synchronizationState;
    }

    @Override
    public DeviceGatewayId gatewayId() {
        return gatewayId;
    }

    public SynchronizationStateEnum getSynchronizationState() {
        return synchronizationState;
    }

    @Override
    public SetGatewaySynchronizationStateCommand withGatewayId(DeviceGatewayId gatewayId) {
        return new SetGatewaySynchronizationStateCommand(gatewayId, synchronizationState);
    }
}
