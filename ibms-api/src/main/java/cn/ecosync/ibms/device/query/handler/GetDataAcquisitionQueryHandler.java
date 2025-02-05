package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetDataAcquisitionQueryHandler implements QueryHandler<GetDataAcquisitionQuery, DeviceDataAcquisition> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;

    @Override
    @Transactional(readOnly = true)
    public DeviceDataAcquisition handle(GetDataAcquisitionQuery query) {
        DeviceDataAcquisitionId dataAcquisitionId = query.getDataAcquisitionId();
        return dataAcquisitionRepository.get(dataAcquisitionId).orElse(null);
    }
}
