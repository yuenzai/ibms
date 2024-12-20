package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.ibms.device.query.ListSearchDeviceDataAcquisitionQuery;
import cn.ecosync.ibms.device.repository.DeviceDataAcquisitionRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchDeviceDataAcquisitionListQueryHandler implements QueryHandler<ListSearchDeviceDataAcquisitionQuery, List<DeviceDataAcquisitionModel>> {
    private final DeviceDataAcquisitionRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDataAcquisitionModel> handle(ListSearchDeviceDataAcquisitionQuery query) {
        Sort sort = query.toSort();
        return repository.search(null, sort);
    }
}
