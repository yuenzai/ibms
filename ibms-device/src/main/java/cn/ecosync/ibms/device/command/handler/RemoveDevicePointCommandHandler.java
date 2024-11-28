package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.command.RemoveDevicePointCommand;
import cn.ecosync.ibms.device.domain.*;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.AggregateSavedEvent;
import cn.ecosync.iframework.event.EventBus;
import cn.ecosync.iframework.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RemoveDevicePointCommandHandler implements CommandHandler<RemoveDevicePointCommand> {
    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(RemoveDevicePointCommand command) {
        DeviceId deviceId = new DeviceId(command.getDeviceCode());
        Device device = deviceRepository.get(deviceId).orElse(null);
        Assert.notNull(device, "device not exist: " + deviceId.getDeviceCode());

        List<DevicePointId> pointIds = CollectionUtils.nullSafeOf(command.getPointCodes()).stream()
                .map(DevicePointId::new)
                .collect(Collectors.toList());

        Map<DevicePointId, DevicePoint> devicePoints = device.devicePoints();
        for (DevicePointId pointId : pointIds) {
            DevicePoint removed = devicePoints.remove(pointId);
            if (removed != null) {
                removed.device(null);
            }
        }

        DeviceDto dto = DeviceMapper.map(device);
        AggregateSavedEvent event = new AggregateSavedEvent(dto);
        eventBus.publish(event);
    }
}
