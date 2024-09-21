package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.AddDeviceCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceProperties;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.system.model.DictionaryKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AddDeviceCommandHandler implements CommandHandler<AddDeviceCommand> {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public void handle(AddDeviceCommand command) {
        DeviceId deviceId = command.toDeviceId();
        DictionaryKey networkId = command.toNetworkId();
        DeviceProperties deviceProperties = command.getProperties();
        Device device = deviceRepository.get(deviceId).orElse(null);
        if (device == null) {
            device = new Device(deviceId, networkId, command.getDeviceName(), command.getPath(), command.getDescription(), deviceProperties);
            deviceRepository.add(device);
        } else {
            device.setDeviceName(command.getDeviceName());
            device.setPath(command.getPath());
            device.setDescription(command.getDescription());
            device.setDeviceProperties(deviceProperties);
        }
    }
}
