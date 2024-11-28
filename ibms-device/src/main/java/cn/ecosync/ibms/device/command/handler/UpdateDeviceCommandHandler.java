package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.command.UpdateDeviceCommand;
import cn.ecosync.ibms.device.domain.Device;
import cn.ecosync.ibms.device.domain.DeviceId;
import cn.ecosync.ibms.device.domain.DeviceProperties;
import cn.ecosync.ibms.device.domain.DeviceRepository;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.AggregateSavedEvent;
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
        DeviceId deviceId = new DeviceId(command.getDeviceCode());

        Device device = deviceRepository.get(deviceId).orElse(null);
        Assert.notNull(device, "device does not exist: " + deviceId.getDeviceCode());

        DeviceProperties deviceProperties = new DeviceProperties(
                command.getDeviceName(), command.getPath(), command.getDescription(), command.getDeviceExtra()
        );

        device.update(deviceProperties);

        DeviceDto dto = DeviceMapper.map(device);
        AggregateSavedEvent event = new AggregateSavedEvent(dto);
        eventBus.publish(event);
    }
}
