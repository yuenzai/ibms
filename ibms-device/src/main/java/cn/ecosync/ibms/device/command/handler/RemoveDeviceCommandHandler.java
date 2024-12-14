package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.RemoveDeviceCommand;
import cn.ecosync.ibms.device.event.DeviceRemovedEvent;
import cn.ecosync.ibms.device.model.DeviceCommandModel;
import cn.ecosync.ibms.device.model.DeviceRepository;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveDeviceCommandHandler implements CommandHandler<RemoveDeviceCommand> {
    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(RemoveDeviceCommand command) {
        DeviceId deviceId = command.getDeviceId();
        DeviceCommandModel device = deviceRepository.get(deviceId).orElse(null);
        if (device != null) {
            deviceRepository.remove(device);
            DeviceRemovedEvent event = new DeviceRemovedEvent(device);
            eventBus.publish(event);
        }
    }
}
