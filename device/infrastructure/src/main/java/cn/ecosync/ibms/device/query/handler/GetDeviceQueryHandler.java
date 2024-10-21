package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceReadonlyRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetDeviceQueryHandler implements QueryHandler<GetDeviceQuery, DeviceDto> {
    private final DeviceRepository deviceRepository;
    private final DeviceReadonlyRepository deviceReadonlyRepository;

    @Override
    @Transactional(readOnly = true)
    public DeviceDto handle(GetDeviceQuery query) {
        if (query.getReadonly()) {
            return deviceReadonlyRepository.findByDeviceCode(query.getDeviceCode()).orElse(null);
        } else {
            return deviceRepository.get(new DeviceId(query.getDeviceCode()))
                    .map(DeviceMapper::mapWithPoints)
                    .orElse(null);
        }
    }
}
