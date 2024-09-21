package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.RemoveDeviceCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveDeviceCommandHandler implements CommandHandler<RemoveDeviceCommand> {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public void handle(RemoveDeviceCommand command) {
        DeviceId deviceId = command.toDeviceId();
        Device device = deviceRepository.get(deviceId).orElse(null);
        if (device != null) {
            for (DevicePoint devicePoint : device.getDevicePoints().values()) {
                devicePoint.setDevice(null);
            }
            device.getDevicePoints().clear();
            deviceRepository.remove(device);
        }
    }
}
