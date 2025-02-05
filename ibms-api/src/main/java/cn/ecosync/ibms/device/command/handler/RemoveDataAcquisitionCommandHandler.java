package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.RemoveDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.event.EventBus;
import org.springframework.transaction.annotation.Transactional;

public class RemoveDataAcquisitionCommandHandler implements CommandHandler<RemoveDataAcquisitionCommand> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private final EventBus eventBus;

    public RemoveDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        this.dataAcquisitionRepository = dataAcquisitionRepository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public void handle(RemoveDataAcquisitionCommand command) {
        DeviceDataAcquisitionId dataAcquisitionId = command.getDataAcquisitionId();
        dataAcquisitionRepository.remove(dataAcquisitionId)
                .forEach(eventBus::publish);
    }
}
