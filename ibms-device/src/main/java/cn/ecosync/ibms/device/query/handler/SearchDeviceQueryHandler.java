package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.dto.DeviceProbe;
import cn.ecosync.ibms.device.jpa.DeviceEntity;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.query.SearchDeviceQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceJpaRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDeviceQueryHandler implements QueryHandler<SearchDeviceQuery, Page<Device>> {
    private final DeviceJpaRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Device> handle(SearchDeviceQuery query) {
        DeviceProbe probe = query.getProbe();
        Example<DeviceEntity> example = deviceRepository.newExample(probe);
        Pageable pageable = query.toPageable();
        return deviceRepository.findAll(example, pageable)
                .map(DeviceEntity::getDevice);
    }
}
