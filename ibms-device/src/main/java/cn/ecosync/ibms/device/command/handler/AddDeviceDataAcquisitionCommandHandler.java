package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.AddDeviceDataAcquisitionCommand;
import cn.ecosync.ibms.device.event.DeviceDataAcquisitionSavedEvent;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
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
public class AddDeviceDataAcquisitionCommandHandler implements CommandHandler<AddDeviceDataAcquisitionCommand> {
    private final DeviceDataAcquisitionRepository repository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(AddDeviceDataAcquisitionCommand command) {
        DeviceDataAcquisitionId daqId = command.getDaqId();
        DeviceDataAcquisitionCommandModel daq = repository.get(daqId).orElse(null);
        Assert.isNull(daq, "daq already exists: " + daqId);
        daq = new DeviceDataAcquisition(daqId, command.getDaqProperties());
        repository.add(daq);

        Event event = new DeviceDataAcquisitionSavedEvent(daq);
        eventBus.publish(event);
    }
}
