package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.SaveDeviceCommand;
import cn.ecosync.ibms.device.jpa.DeviceEntity;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.repository.jpa.DeviceJpaRepository;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class SaveDeviceCommandHandler implements CommandHandler<SaveDeviceCommand> {
    private final DeviceJpaRepository deviceRepository;

    @Override
    @Transactional
    public void handle(SaveDeviceCommand command) {
        Device device = command.getDevice();
        DeviceEntity deviceEntity = deviceRepository.findByDeviceId(device.getDeviceId()).orElse(null);
        Assert.notNull(deviceEntity, "Device does not exist");
        deviceEntity.save(device);
    }
}
