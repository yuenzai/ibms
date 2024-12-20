package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.UpdateDeviceDataAcquisitionCommand;
import cn.ecosync.ibms.device.event.DeviceDataAcquisitionSavedEvent;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionCommandModel;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.repository.DeviceDataAcquisitionRepository;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class UpdateDeviceDataAcquisitionCommandHandler implements CommandHandler<UpdateDeviceDataAcquisitionCommand> {
    private final DeviceDataAcquisitionRepository repository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(UpdateDeviceDataAcquisitionCommand command) {
        DeviceDataAcquisitionId daqId = command.getDaqId();
        DeviceDataAcquisitionCommandModel daq = repository.get(daqId).orElse(null);
        Assert.notNull(daq, "daq must not be null");
        daq.update(command.getDaqProperties());

        Event event = new DeviceDataAcquisitionSavedEvent(daq);
        eventBus.publish(event);
    }
}
