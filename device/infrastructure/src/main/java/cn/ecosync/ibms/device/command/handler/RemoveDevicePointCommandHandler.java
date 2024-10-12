package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.command.RemoveDevicePointCommand;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.event.AggregateSavedEvent;
import cn.ecosync.ibms.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RemoveDevicePointCommandHandler implements CommandHandler<RemoveDevicePointCommand> {
    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(RemoveDevicePointCommand command) {
        DeviceId deviceId = command.getDeviceId();
        Device device = deviceRepository.get(deviceId).orElse(null);
        Assert.notNull(device, "device not exist: " + deviceId.getDeviceCode());
        List<DevicePointId> pointIds = command.toDevicePointIds();
        Map<DevicePointId, DevicePoint> devicePoints = device.devicePoints();
        for (DevicePointId pointId : pointIds) {
            DevicePoint removed = devicePoints.remove(pointId);
            if (removed != null) {
                removed.device(null);
            }
        }

        DeviceDto dto = DeviceMapper.mapOf(device);
        AggregateSavedEvent event = new AggregateSavedEvent(dto);
        eventBus.publish(event);
    }
}
