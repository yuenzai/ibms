package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.PutDevicePointCommand;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DevicePoint;
import cn.ecosync.ibms.device.model.DevicePointId;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PutDevicePointCommandHandler implements CommandHandler<PutDevicePointCommand> {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public void handle(PutDevicePointCommand command) {
        DeviceId deviceId = command.toDeviceId();
        Device device = deviceRepository.get(deviceId).orElse(null);
        if (device == null) {
            return;
        }
        for (PutDevicePointCommand.Point dto : command.getDevicePoints()) {
            DevicePointId devicePointId = dto.toDevicePointId();
            DevicePoint devicePoint = device.getDevicePoints().get(devicePointId);
            if (devicePoint == null) {
                devicePoint = new DevicePoint(device, devicePointId, dto.getPointName(), dto.getPointProperties());
                device.getDevicePoints().put(devicePointId, devicePoint);
            } else {
                devicePoint.setDevicePointProperties(dto.getPointProperties());
            }
        }
    }
}
