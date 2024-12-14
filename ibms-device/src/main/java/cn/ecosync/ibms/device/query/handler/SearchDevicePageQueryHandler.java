package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceRepository;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.ibms.device.query.PageSearchDeviceQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDevicePageQueryHandler implements QueryHandler<PageSearchDeviceQuery, Page<DeviceModel>> {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceModel> handle(PageSearchDeviceQuery query) {
        Pageable pageable = query.toPageable();
        DeviceDataAcquisitionId daqId = query.toDeviceDataAcquisitionId();
        DeviceModel probe = deviceRepository.newProbe(daqId, null, null);
        return deviceRepository.search(probe, pageable);
    }
}
