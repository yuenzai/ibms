package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.RemoveGatewayCommand;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.repository.jpa.DeviceGatewayJpaRepository;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveGatewayCommandHandler implements CommandHandler<RemoveGatewayCommand> {
    private final DeviceGatewayJpaRepository gatewayRepository;

    @Override
    @Transactional
    public void handle(RemoveGatewayCommand command) {
        DeviceGatewayId gatewayId = command.gatewayId();
        gatewayRepository.removeByGatewayId(gatewayId);
    }
}
