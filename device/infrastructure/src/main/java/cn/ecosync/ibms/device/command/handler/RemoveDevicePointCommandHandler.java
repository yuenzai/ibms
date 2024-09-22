package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.RemoveDevicePointCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveDevicePointCommandHandler implements CommandHandler<RemoveDevicePointCommand> {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public void handle(RemoveDevicePointCommand command) {
        DeviceId deviceId = command.toDeviceId();
        Device device = deviceRepository.get(deviceId).orElse(null);
        if (device == null) {
            return;
        }
        List<DevicePointId> pointIds = command.toDevicePointIds();
        for (DevicePointId pointId : pointIds) {
            DevicePoint removed = device.getDevicePoints().remove(pointId);
            if (removed != null) {
                removed.setDevice(null);
            }
        }
    }
}
