package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.GatewaySynchronizationService;
import cn.ecosync.ibms.device.command.SetGatewaySynchronizationStateCommand;
import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.jpa.DeviceGatewayJpaRepository;
import cn.ecosync.ibms.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class SetGatewaySynchronizationStateCommandHandler implements CommandHandler<SetGatewaySynchronizationStateCommand> {
    private final DeviceGatewayJpaRepository gatewayRepository;
    private final GatewaySynchronizationService gatewaySynchronizationService;

    @Override
    @Transactional
    public void handle(SetGatewaySynchronizationStateCommand command) {
        DeviceGatewayId gatewayId = command.gatewayId();
        DeviceGatewayEntity gatewayEntity = gatewayRepository.findByGatewayId(gatewayId).orElse(null);
        Assert.notNull(gatewayEntity, "Gateway not exists");
        DeviceGateway gateway = gatewayEntity.getGateway()
                .withSynchronizationState(command.getSynchronizationState());
        gatewayEntity.save(gateway);
        gatewaySynchronizationService.notify(gatewayId);
    }
}
