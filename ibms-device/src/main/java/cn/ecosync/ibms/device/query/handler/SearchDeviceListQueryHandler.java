package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.*;
import cn.ecosync.ibms.device.query.ListSearchDeviceQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchDeviceListQueryHandler implements QueryHandler<ListSearchDeviceQuery, List<DeviceModel>> {
    private final DeviceRepository<DeviceModel> deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceModel> handle(ListSearchDeviceQuery query) {
        Sort sort = query.toSort();
        DeviceId deviceIdProbe = DeviceId.newProbe(query.getDeviceCode());
        DeviceDataAcquisitionId daqIdProbe = DeviceDataAcquisitionId.newProbe(query.getDaqName());
        DeviceProperties devicePropertiesProbe = DeviceProperties.newProbe(query.getDeviceName());
        Example<DeviceModel> example = deviceRepository.newExample(deviceIdProbe, daqIdProbe, devicePropertiesProbe);
        return deviceRepository.search(example, sort);
    }
}
