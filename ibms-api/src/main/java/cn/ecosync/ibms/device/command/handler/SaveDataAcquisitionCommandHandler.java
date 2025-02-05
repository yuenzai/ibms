package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.transaction.annotation.Transactional;

public class SaveDataAcquisitionCommandHandler implements CommandHandler<SaveDataAcquisitionCommand>, ApplicationEventPublisherAware {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private ApplicationEventPublisher eventPublisher;

    public SaveDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        this.dataAcquisitionRepository = dataAcquisitionRepository;
    }

    @Override
    @Transactional
    public void handle(SaveDataAcquisitionCommand command) {
        dataAcquisitionRepository.save(command)
                .forEach(eventPublisher::publishEvent);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
