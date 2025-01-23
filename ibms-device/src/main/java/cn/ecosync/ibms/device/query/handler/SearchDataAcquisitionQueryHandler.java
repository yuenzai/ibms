package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.query.SearchDataAcquisitionQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceDataAcquisitionJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDataAcquisitionQueryHandler implements QueryHandler<SearchDataAcquisitionQuery, Page<DeviceDataAcquisition>> {
    private final DeviceDataAcquisitionJpaRepository dataAcquisitionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDataAcquisition> handle(SearchDataAcquisitionQuery query) {
        return dataAcquisitionRepository.findAll(query.toPageable())
                .map(in -> {
                    DeviceDataAcquisition dataAcquisition = in.getDataAcquisition();
                    return new DeviceDataAcquisition(dataAcquisition.getDataAcquisitionId(), dataAcquisition.getScrapeInterval());
                });
    }
}
