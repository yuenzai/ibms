package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.event.EventBus;
import org.springframework.transaction.annotation.Transactional;

public class SaveDataAcquisitionCommandHandler implements CommandHandler<SaveDataAcquisitionCommand> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private final EventBus eventBus;

    public SaveDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        this.dataAcquisitionRepository = dataAcquisitionRepository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public void handle(SaveDataAcquisitionCommand command) {
        dataAcquisitionRepository.save(command)
                .forEach(eventBus::publish);
    }
}
