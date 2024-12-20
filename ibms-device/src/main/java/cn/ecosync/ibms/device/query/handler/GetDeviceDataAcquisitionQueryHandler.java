package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.query.GetDeviceDataAcquisitionQuery;
import cn.ecosync.ibms.device.repository.DeviceDataAcquisitionRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetDeviceDataAcquisitionQueryHandler implements QueryHandler<GetDeviceDataAcquisitionQuery, DeviceDataAcquisitionModel> {
    private final DeviceDataAcquisitionRepository repository;

    @Override
    @Transactional(readOnly = true)
    public DeviceDataAcquisitionModel handle(GetDeviceDataAcquisitionQuery query) {
        return repository.get(query.getDaqId()).orElse(null);
    }
}
