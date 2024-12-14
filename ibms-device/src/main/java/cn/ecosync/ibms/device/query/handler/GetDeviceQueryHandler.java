package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceRepository;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetDeviceQueryHandler implements QueryHandler<GetDeviceQuery, DeviceModel> {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public DeviceModel handle(GetDeviceQuery query) {
        return deviceRepository.get(query.getDeviceId()).orElse(null);
    }
}
