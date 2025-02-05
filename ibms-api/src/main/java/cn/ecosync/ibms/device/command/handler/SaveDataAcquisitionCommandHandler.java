package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.event.DeviceDataAcquisitionChangedEvent;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SaveDataAcquisitionCommandHandler implements CommandHandler<SaveDataAcquisitionCommand>, ApplicationEventPublisherAware {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void handle(SaveDataAcquisitionCommand command) {
        DeviceDataAcquisitionId dataAcquisitionId = command.getDataAcquisitionId();
        DeviceDataAcquisition dataAcquisition = dataAcquisitionRepository.get(dataAcquisitionId).orElse(null);
        if (dataAcquisition == null) {
            dataAcquisition = new DeviceDataAcquisition(dataAcquisitionId, command.getScrapeInterval());
        } else {
            dataAcquisition = dataAcquisition.builder()
                    .with(command.getScrapeInterval())
                    .with(command.getDataPoints())
                    .with(command.getSynchronizationState())
                    .build();
        }
        dataAcquisitionRepository.save(dataAcquisition);
        DeviceDataAcquisitionChangedEvent event = new DeviceDataAcquisitionChangedEvent(dataAcquisition);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
