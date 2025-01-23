package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.AddDataAcquisitionCommand;
import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class AddDataAcquisitionCommandHandler implements CommandHandler<AddDataAcquisitionCommand> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;

    @Override
    @Transactional
    public void handle(AddDataAcquisitionCommand command) {
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(command.getDataAcquisitionCode());
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        Assert.isNull(dataAcquisitionEntity, "DataAcquisitionEntity already exist");

        DeviceDataAcquisition deviceDataAcquisition = new DeviceDataAcquisition(dataAcquisitionId, command.getScrapeInterval());
        dataAcquisitionEntity = new DeviceDataAcquisitionEntity(deviceDataAcquisition);
        dataAcquisitionRepository.save(dataAcquisitionEntity);
    }
}
