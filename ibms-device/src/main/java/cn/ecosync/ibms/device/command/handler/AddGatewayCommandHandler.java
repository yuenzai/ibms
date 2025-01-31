package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.AddGatewayCommand;
import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.repository.jpa.DeviceGatewayJpaRepository;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class AddGatewayCommandHandler implements CommandHandler<AddGatewayCommand> {
    private final DeviceGatewayJpaRepository gatewayRepository;

    @Override
    @Transactional
    public void handle(AddGatewayCommand command) {
        DeviceGatewayId gatewayId = command.gatewayId();
        DeviceGatewayEntity gatewayEntity = gatewayRepository.findByGatewayId(gatewayId).orElse(null);
        Assert.isNull(gatewayEntity, "Gateway already exists");
        DeviceGateway gateway = new DeviceGateway(gatewayId, null, null, null);
        gatewayEntity = new DeviceGatewayEntity(gateway);
        gatewayRepository.save(gatewayEntity);
    }
}
