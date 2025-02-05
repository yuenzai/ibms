package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
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
