package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.SaveDeviceSchemasCommand;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.repository.jpa.DeviceSchemasJpaRepository;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SaveDeviceSchemasCommandHandler implements CommandHandler<SaveDeviceSchemasCommand> {
    private final DeviceSchemasJpaRepository schemasRepository;

    @Override
    @Transactional
    public void handle(SaveDeviceSchemasCommand command) {
        DeviceSchemas schemas = command.getSchemas();
        DeviceSchemasId schemasId = schemas.getSchemasId();
        DeviceSchemasEntity schemasEntity = schemasRepository.findBySchemasId(schemasId).orElse(null);
        if (schemasEntity == null) {
            schemasEntity = new DeviceSchemasEntity(schemas);
            schemasRepository.save(schemasEntity);
        } else {
            schemasEntity.save(schemas);
        }
    }
}
