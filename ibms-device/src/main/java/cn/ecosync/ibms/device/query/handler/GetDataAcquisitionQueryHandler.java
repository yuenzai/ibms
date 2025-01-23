package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.IDeviceDataAcquisition;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetDataAcquisitionQueryHandler implements QueryHandler<GetDataAcquisitionQuery, IDeviceDataAcquisition> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;

    @Override
    @Transactional(readOnly = true)
    public IDeviceDataAcquisition handle(GetDataAcquisitionQuery query) {
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(query.getDataAcquisitionCode());
        return dataAcquisitionRepository.findByDataAcquisitionId(dataAcquisitionId)
                .map(DeviceDataAcquisitionEntity::getDataAcquisition)
                .orElse(null);
    }
}
