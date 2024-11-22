package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.command.AddDeviceCommand;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.domain.device.Device;
import cn.ecosync.ibms.domain.device.DeviceId;
import cn.ecosync.ibms.domain.device.DeviceProperties;
import cn.ecosync.ibms.domain.device.DeviceRepository;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.AggregateSavedEvent;
import cn.ecosync.iframework.event.EventBus;
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
        DeviceId deviceId = new DeviceId(command.getDeviceCode());
        DeviceProperties deviceProperties = new DeviceProperties(
                command.getDeviceName(), command.getPath(), command.getDescription(), command.getDeviceExtra()
        );

        Device device = deviceRepository.get(deviceId).orElse(null);
        Assert.isNull(device, "Device already exists: " + deviceId.getDeviceCode());
        device = new Device(deviceId, deviceProperties);
        deviceRepository.add(device);

        DeviceDto dto = DeviceMapper.map(device);
        AggregateSavedEvent event = new AggregateSavedEvent(dto);
        eventBus.publish(event);
    }
}
