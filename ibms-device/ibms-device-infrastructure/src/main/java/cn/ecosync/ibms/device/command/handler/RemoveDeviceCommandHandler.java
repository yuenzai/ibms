package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.DeviceConstant;
import cn.ecosync.ibms.device.command.RemoveDeviceCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.event.AggregateRemovedEvent;
import cn.ecosync.ibms.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RemoveDeviceCommandHandler implements CommandHandler<RemoveDeviceCommand> {
    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    //todo hibernate 执行的 sql 过多需要优化
    @Override
    @Transactional
    public void handle(RemoveDeviceCommand command) {
        DeviceId deviceId = new DeviceId(command.getDeviceCode());

        Device device = deviceRepository.get(deviceId).orElse(null);
        if (device != null) {
            Map<DevicePointId, DevicePoint> devicePoints = device.devicePoints();

            for (DevicePoint devicePoint : devicePoints.values()) {
                devicePoint.device(null);
            }
            devicePoints.clear();
            deviceRepository.remove(device);

            eventBus.publish(new AggregateRemovedEvent(DeviceConstant.AGGREGATE_TYPE, deviceId.getDeviceCode()));
        }
    }
}
