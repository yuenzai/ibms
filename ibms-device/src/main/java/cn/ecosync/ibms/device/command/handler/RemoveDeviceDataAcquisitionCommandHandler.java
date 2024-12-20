package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.RemoveDeviceDataAcquisitionCommand;
import cn.ecosync.ibms.device.event.DeviceDataAcquisitionRemovedEvent;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionCommandModel;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.repository.DeviceDataAcquisitionDeletedRepository;
import cn.ecosync.ibms.device.repository.DeviceDataAcquisitionRepository;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveDeviceDataAcquisitionCommandHandler implements CommandHandler<RemoveDeviceDataAcquisitionCommand> {
    private final DeviceDataAcquisitionRepository repository;
    private final DeviceDataAcquisitionDeletedRepository deletedRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(RemoveDeviceDataAcquisitionCommand command) {
        DeviceDataAcquisitionId daqId = command.getDaqId();
        DeviceDataAcquisitionCommandModel daq = repository.get(daqId).orElse(null);
        if (daq != null) {
            repository.remove(daq);
            deletedRepository.add(daq);

            Event event = new DeviceDataAcquisitionRemovedEvent(daq);
            eventBus.publish(event);
        }
    }
}
