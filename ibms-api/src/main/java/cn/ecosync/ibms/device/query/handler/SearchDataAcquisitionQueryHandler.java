package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.device.query.SearchDataAcquisitionQuery;
import cn.ecosync.ibms.query.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public class SearchDataAcquisitionQueryHandler implements QueryHandler<SearchDataAcquisitionQuery, Page<DeviceDataAcquisition>> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;

    public SearchDataAcquisitionQueryHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        this.dataAcquisitionRepository = dataAcquisitionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDataAcquisition> handle(SearchDataAcquisitionQuery query) {
        return dataAcquisitionRepository.search(query.toPageable());
    }
}
