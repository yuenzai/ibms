package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.RemoveDeviceSchemasCommand;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.jpa.DeviceSchemasJpaRepository;
import cn.ecosync.ibms.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveSchemasCommandHandler implements CommandHandler<RemoveDeviceSchemasCommand> {
    private final DeviceSchemasJpaRepository schemasRepository;

    @Override
    @Transactional
    public void handle(RemoveDeviceSchemasCommand command) {
        DeviceSchemasId schemasId = new DeviceSchemasId(command.getSchemasCode());
        schemasRepository.removeBySchemasId(schemasId);
    }
}
