package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.jpa.DeviceEntity;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.device.jpa.DeviceJpaRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetDeviceQueryHandler implements QueryHandler<GetDeviceQuery, Device> {
    private final DeviceJpaRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public Device handle(GetDeviceQuery query) {
        DeviceId deviceId = new DeviceId(query.getDeviceCode());
        return deviceRepository.findByDeviceId(deviceId)
                .map(DeviceEntity::getDevice)
                .orElse(null);
    }
}
