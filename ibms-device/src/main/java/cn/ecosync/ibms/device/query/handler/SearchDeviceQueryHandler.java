package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.dto.DeviceProbe;
import cn.ecosync.ibms.device.jpa.DeviceEntity;
import cn.ecosync.ibms.device.query.SearchDeviceQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchDeviceQueryHandler implements QueryHandler<SearchDeviceQuery, List<Device>> {
    private final DeviceJpaRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Device> handle(SearchDeviceQuery query) {
        DeviceProbe probe = query.getProbe();
        Example<DeviceEntity> example = deviceRepository.newExample(probe);
        Pageable pageable = query.toPageable();
        List<DeviceEntity> deviceEntities = pageable.isPaged()
                ? deviceRepository.findAll(example, pageable).getContent()
                : deviceRepository.findAll(example, pageable.getSort());
        return deviceEntities.stream()
                .map(DeviceEntity::getDevice)
                .collect(Collectors.toList());
    }
}
