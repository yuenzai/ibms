package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.jpa.DeviceDataAcquisitionEntity;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetDataAcquisitionQueryHandler implements QueryHandler<GetDataAcquisitionQuery, DeviceDataAcquisition> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;

    @Override
    @Transactional(readOnly = true)
    public DeviceDataAcquisition handle(GetDataAcquisitionQuery query) {
        return dataAcquisitionRepository.findByDataAcquisitionId(query.getDataAcquisitionId())
                .map(DeviceDataAcquisitionEntity::getDataAcquisition)
                .orElse(null);
    }
}
