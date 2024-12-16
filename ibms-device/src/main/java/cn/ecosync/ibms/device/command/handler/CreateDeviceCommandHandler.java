package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.CreateDeviceCommand;
import cn.ecosync.ibms.device.event.DeviceSavedEvent;
import cn.ecosync.ibms.device.model.*;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.event.EventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateDeviceCommandHandler implements CommandHandler<CreateDeviceCommand> {
    private final DeviceDataAcquisitionRepository daqRepository;
    private final DeviceRepository<DeviceModel> deviceRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(CreateDeviceCommand command) {
        DeviceDataAcquisitionId daqId = command.getDaqId();
        DeviceDataAcquisitionCommandModel daq = daqRepository.get(daqId).orElse(null);
        Assert.notNull(daq, "daq does not exist: " + daqId.toStringId());

        List<DeviceModel> devices = command.newDevices();
        if (CollectionUtils.isEmpty(devices)) return;

        List<Event> events = new ArrayList<>();
        for (DeviceModel device : devices) {
            DeviceId deviceId = device.getDeviceId();
            DeviceCommandModel deviceCommandModel = deviceRepository.get(deviceId).orElse(null);
            if (deviceCommandModel != null) {
                log.debug("device exists: {}", deviceId.toStringId());
                continue;
            }

            Device deviceEntity = new Device(deviceId, daqId, device.getDeviceProperties());
            deviceRepository.add(deviceEntity);
            DeviceSavedEvent event = new DeviceSavedEvent(deviceEntity);
            events.add(event);
        }

        events.forEach(eventBus::publish);
    }
}
