package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.SetDataAcquisitionSynchronizationStateCommand;
import cn.ecosync.ibms.device.event.DeviceDataAcquisitionSynchronizationStateChangedEvent;
import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetDataAcquisitionSynchronizationStateCommandHandler implements CommandHandler<SetDataAcquisitionSynchronizationStateCommand>, ApplicationEventPublisherAware {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void handle(SetDataAcquisitionSynchronizationStateCommand command) {
        DeviceDataAcquisitionId dataAcquisitionId = command.getDataAcquisitionId();
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity != null) {
            DeviceDataAcquisition dataAcquisition = dataAcquisitionEntity.getDataAcquisition().builder()
                    .with(command.getSynchronizationState())
                    .build();
            dataAcquisitionEntity.save(dataAcquisition);
        }
        DeviceDataAcquisitionSynchronizationStateChangedEvent event = new DeviceDataAcquisitionSynchronizationStateChangedEvent(dataAcquisitionId, command.getSynchronizationState());
        eventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
