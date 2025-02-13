package cn.ecosync.ibms.gateway.query.handler;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.query.QueryHandler;
import org.springframework.transaction.annotation.Transactional;

public class GetDataAcquisitionQueryHandler implements QueryHandler<GetDataAcquisitionQuery, DeviceDataAcquisition> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;

    public GetDataAcquisitionQueryHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        this.dataAcquisitionRepository = dataAcquisitionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceDataAcquisition handle(GetDataAcquisitionQuery query) {
        DeviceDataAcquisitionId dataAcquisitionId = query.getDataAcquisitionId();
        return dataAcquisitionRepository.get(dataAcquisitionId).orElse(null);
    }
}
