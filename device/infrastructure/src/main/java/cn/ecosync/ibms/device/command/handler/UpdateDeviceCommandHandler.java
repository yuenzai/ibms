package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.UpdateDeviceCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateDeviceCommandHandler implements CommandHandler<UpdateDeviceCommand> {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public void handle(UpdateDeviceCommand command) {
        DeviceId deviceId = command.toDeviceId();
        Device device = deviceRepository.get(deviceId).orElse(null);
        if (device == null) {
            return;
        }
        device.setDeviceName(command.getDeviceName());
        device.setPath(command.getPath());
        device.setDescription(command.getDescription());
        device.setDeviceProperties(command.getProperties());
    }
}
