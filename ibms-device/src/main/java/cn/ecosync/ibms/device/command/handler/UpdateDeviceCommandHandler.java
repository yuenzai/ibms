package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.UpdateDeviceCommand;
import cn.ecosync.ibms.device.event.DeviceSavedEvent;
import cn.ecosync.ibms.device.model.DeviceCommandModel;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class UpdateDeviceCommandHandler implements CommandHandler<UpdateDeviceCommand> {
    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(UpdateDeviceCommand command) {
        DeviceId deviceId = command.getDeviceId();
        DeviceCommandModel device = deviceRepository.get(deviceId).orElse(null);
        Assert.notNull(device, "device does not exist: " + deviceId.toStringId());
        device.update(command.getDeviceProperties());
        DeviceSavedEvent event = new DeviceSavedEvent(device);
        eventBus.publish(event);
    }
}
