package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.query.PageSearchDeviceDataAcquisitionQuery;
import cn.ecosync.ibms.device.repository.DeviceDataAcquisitionRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDeviceDataAcquisitionPageQueryHandler implements QueryHandler<PageSearchDeviceDataAcquisitionQuery, Page<DeviceDataAcquisitionModel>> {
    private final DeviceDataAcquisitionRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDataAcquisitionModel> handle(PageSearchDeviceDataAcquisitionQuery query) {
        Pageable pageable = query.toPageable();
        return repository.search(null, pageable);
    }
}
