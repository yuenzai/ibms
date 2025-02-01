package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition;
import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.ibms.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SaveDataAcquisitionDataPointsCommandHandler implements CommandHandler<SaveDataAcquisitionCommand> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;

    @Override
    @Transactional
    public void handle(SaveDataAcquisitionCommand command) {
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(command.getDataAcquisitionCode());
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        Assert.notNull(dataAcquisitionEntity, "DataAcquisitionEntity not exists");

        DeviceDataAcquisition newDataAcquisition = null;
        DeviceDataAcquisition dataAcquisition = dataAcquisitionEntity.getDataAcquisition();
        if (Objects.equals(command.getClass(), SaveDataAcquisitionCommand.class)) {
            newDataAcquisition = dataAcquisition.withScrapeInterval(command.getScrapeInterval());
        } else if (command instanceof SaveDataAcquisitionCommand.Bacnet) {
            SaveDataAcquisitionCommand.Bacnet bacnetCommand = (SaveDataAcquisitionCommand.Bacnet) command;
            if (command.getScrapeInterval() != null) {
                newDataAcquisition = new BacnetDataAcquisition(dataAcquisition.getDataAcquisitionId(), command.getScrapeInterval(), bacnetCommand.getDataPoints());
            } else {
                newDataAcquisition = new BacnetDataAcquisition(dataAcquisition, bacnetCommand.getDataPoints());
            }
        }
        if (newDataAcquisition != null) dataAcquisitionEntity.save(newDataAcquisition);
    }
}
