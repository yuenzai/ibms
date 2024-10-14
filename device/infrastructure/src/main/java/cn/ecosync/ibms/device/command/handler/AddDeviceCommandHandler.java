package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.command.AddDeviceCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceProperties;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.event.AggregateSavedEvent;
import cn.ecosync.ibms.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class AddDeviceCommandHandler implements CommandHandler<AddDeviceCommand> {
    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(AddDeviceCommand command) {
        DeviceId deviceId = command.getDeviceId();
        DeviceProperties deviceProperties = command.getDeviceProperties();

        Device device = deviceRepository.get(deviceId).orElse(null);
        Assert.isNull(device, "Device already exists: " + deviceId.getDeviceCode());
        device = new Device(deviceId, deviceProperties);
        deviceRepository.add(device);

        DeviceDto dto = DeviceMapper.map(device);
        AggregateSavedEvent event = new AggregateSavedEvent(dto);
        eventBus.publish(event);
    }
}
