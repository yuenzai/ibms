package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.SyncGatewayCommand;
import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.repository.jpa.DeviceGatewayJpaRepository;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum.SYNCHRONIZING;

@Component
@RequiredArgsConstructor
public class SyncGatewayCommandHandler implements CommandHandler<SyncGatewayCommand> {
    private final DeviceGatewayJpaRepository gatewayRepository;

    @Override
    @Transactional
    public void handle(SyncGatewayCommand command) {
        DeviceGatewayId gatewayId = new DeviceGatewayId(command.getGatewayCode());
        DeviceGatewayEntity gatewayEntity = gatewayRepository.findByGatewayId(gatewayId).orElse(null);
        Assert.notNull(gatewayEntity, "Gateway not exists");
        DeviceGateway gateway = gatewayEntity.getGateway().withSynchronizationState(SYNCHRONIZING);
        gatewayEntity.save(gateway);
    }
}
