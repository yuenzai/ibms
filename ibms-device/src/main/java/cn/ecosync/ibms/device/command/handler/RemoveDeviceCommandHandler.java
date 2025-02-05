package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.device.command.RemoveDeviceCommand;
import cn.ecosync.ibms.device.jpa.DeviceEntity;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.jpa.DeviceJpaRepository;
import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RemoveDeviceCommandHandler implements CommandHandler<RemoveDeviceCommand> {
    private final DeviceJpaRepository deviceRepository;

    @Override
    @Transactional
    public void handle(RemoveDeviceCommand command) {
        Set<DeviceId> deviceIds = command.getDeviceCodes().stream()
                .filter(StringUtils::hasText)
                .map(DeviceId::new)
                .collect(Collectors.toSet());
        List<DeviceEntity> deviceEntities = deviceRepository.findByDeviceIdIn(deviceIds);
        for (DeviceEntity deviceEntity : deviceEntities) {
//            deviceEntity.clear();
            deviceRepository.delete(deviceEntity);
        }
    }
}
