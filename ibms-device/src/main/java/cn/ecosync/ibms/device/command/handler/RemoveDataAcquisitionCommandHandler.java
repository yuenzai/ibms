package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.RemoveDataAcquisitionCommand;
import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveDataAcquisitionCommandHandler implements CommandHandler<RemoveDataAcquisitionCommand> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;

    @Override
    @Transactional
    public void handle(RemoveDataAcquisitionCommand command) {
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(command.getDataAcquisitionCode());
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity != null) {
            dataAcquisitionEntity.clear();
            dataAcquisitionRepository.delete(dataAcquisitionEntity);
        }
    }
}
