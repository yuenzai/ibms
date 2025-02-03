package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition.DeviceDataAcquisitionBuilder;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SaveDataAcquisitionCommandHandler implements CommandHandler<SaveDataAcquisitionCommand> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;

    @Override
    @Transactional
    public void handle(SaveDataAcquisitionCommand command) {
        DeviceDataAcquisitionId dataAcquisitionId = command.getDataAcquisitionId();
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity == null) {
            DeviceDataAcquisition dataAcquisition = new DeviceDataAcquisition(dataAcquisitionId, command.getScrapeInterval(), null);
            dataAcquisitionEntity = new DeviceDataAcquisitionEntity(dataAcquisition);
            dataAcquisitionRepository.save(dataAcquisitionEntity);
        } else {
            DeviceDataAcquisitionBuilder builder = dataAcquisitionEntity.getDataAcquisition().builder()
                    .with(command.getScrapeInterval());
            if (command instanceof SaveDataAcquisitionCommand.Bacnet) {
                SaveDataAcquisitionCommand.Bacnet bacnetCommand = (SaveDataAcquisitionCommand.Bacnet) command;
                builder = builder.asBacnetBuilder()
                        .with(bacnetCommand.getDataPoints());
            }
            DeviceDataAcquisition dataAcquisition = builder.build();
            dataAcquisitionEntity.save(dataAcquisition);
        }
    }
}
