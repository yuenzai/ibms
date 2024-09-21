package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.PutDeviceCommand;
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
public class PutDeviceCommandHandler implements CommandHandler<PutDeviceCommand> {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public void handle(PutDeviceCommand command) {
        DeviceId deviceId = command.toDeviceId();
        DictionaryKey networkId = command.toNetworkId();
        DeviceProperties deviceProperties = command.getProperties();
        Device device = deviceRepository.get(deviceId).orElse(null);
        if (device == null) {
            device = new Device(deviceId, networkId, command.getDeviceName(), command.getPath(), command.getDescription(), deviceProperties);
            deviceRepository.put(device);
        } else {
            device.setDeviceName(command.getDeviceName());
            device.setPath(command.getPath());
            device.setDescription(command.getDescription());
            device.setDeviceProperties(deviceProperties);
        }
    }
}
