package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.command.UpdateDeviceCommand;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.event.AggregateSavedEvent;
import cn.ecosync.ibms.event.EventBus;
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

        Device device = deviceRepository.get(deviceId).orElse(null);
        Assert.notNull(device, "device does not exist: " + deviceId.getDeviceCode());
        device.update(command.getDeviceProperties());

        DeviceDto dto = DeviceMapper.mapOf(device);
        AggregateSavedEvent event = new AggregateSavedEvent(dto);
        eventBus.publish(event);
    }
}
