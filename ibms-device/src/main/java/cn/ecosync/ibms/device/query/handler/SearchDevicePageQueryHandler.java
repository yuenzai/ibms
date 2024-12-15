package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.*;
import cn.ecosync.ibms.device.query.PageSearchDeviceQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDevicePageQueryHandler implements QueryHandler<PageSearchDeviceQuery, Page<DeviceModel>> {
    private final DeviceRepository<DeviceModel> deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceModel> handle(PageSearchDeviceQuery query) {
        Pageable pageable = query.toPageable();
        DeviceId deviceIdProbe = DeviceId.newProbe(query.getDeviceCode());
        DeviceDataAcquisitionId daqIdProbe = DeviceDataAcquisitionId.newProbe(query.getDaqName());
        DeviceProperties devicePropertiesProbe = DeviceProperties.newProbe(query.getDeviceName(), query.getPath());
        Example<DeviceModel> example = deviceRepository.newExample(deviceIdProbe, daqIdProbe, devicePropertiesProbe);
        return deviceRepository.search(example, pageable);
    }
}
