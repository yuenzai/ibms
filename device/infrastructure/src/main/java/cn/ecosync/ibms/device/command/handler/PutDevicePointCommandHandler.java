package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.command.PutDevicePointCommand;
import cn.ecosync.ibms.device.model.*;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.event.AggregateSavedEvent;
import cn.ecosync.ibms.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PutDevicePointCommandHandler implements CommandHandler<PutDevicePointCommand> {
    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(PutDevicePointCommand command) {
        DeviceId deviceId = command.getDeviceId();
        Device device = deviceRepository.get(deviceId).orElse(null);
        Assert.notNull(device, "device not exist: " + deviceId.getDeviceCode());

        Map<DevicePointId, DevicePoint> devicePoints = device.devicePoints();
        for (DevicePointDto dto : command.getDevicePoints()) {
            DevicePointId pointId = new DevicePointId(dto.getPointCode());
            DevicePoint devicePoint = devicePoints.get(pointId);
            DevicePointProperties pointProperties = new DevicePointProperties(dto.getPointName(), dto.getPointExtra());
            if (devicePoint == null) {
                devicePoint = new DevicePoint(device, pointId, pointProperties);
                devicePoints.put(pointId, devicePoint);
            } else {
                devicePoint.update(pointProperties);
            }
        }

        DeviceDto dto = DeviceMapper.map(device);
        AggregateSavedEvent event = new AggregateSavedEvent(dto);
        eventBus.publish(event);
    }
}
