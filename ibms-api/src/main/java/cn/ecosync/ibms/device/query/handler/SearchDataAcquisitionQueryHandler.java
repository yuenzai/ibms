package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.device.query.SearchDataAcquisitionQuery;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDataAcquisitionQueryHandler implements QueryHandler<SearchDataAcquisitionQuery, Page<DeviceDataAcquisition>> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDataAcquisition> handle(SearchDataAcquisitionQuery query) {
        return dataAcquisitionRepository.search(query.toPageable());
    }
}
