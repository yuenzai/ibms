package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceRepository;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.ibms.device.query.ListSearchDeviceQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchDeviceListQueryHandler implements QueryHandler<ListSearchDeviceQuery, List<DeviceModel>> {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceModel> handle(ListSearchDeviceQuery query) {
        Sort sort = query.toSort();
        DeviceDataAcquisitionId daqId = query.toDeviceDataAcquisitionId();
        DeviceModel probe = deviceRepository.newProbe(daqId, null, null);
        return deviceRepository.search(probe, sort);
    }
}
