package cn.ecosync.aiot.edge.gateway.query.handler;

import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisition;
import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.aiot.edge.gateway.query.GetDataAcquisitionQuery;
import cn.ecosync.aiot.query.QueryHandler;
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
