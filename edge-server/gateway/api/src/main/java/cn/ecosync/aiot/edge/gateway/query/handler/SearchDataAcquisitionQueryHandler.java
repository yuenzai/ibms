package cn.ecosync.aiot.edge.gateway.query.handler;

import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisition;
import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.aiot.edge.gateway.query.SearchDataAcquisitionQuery;
import cn.ecosync.aiot.query.QueryHandler;
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
