package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.query.SearchDeviceQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceReadonlyJpaRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@ConditionalOnClass(EntityManager.class)
public class SearchDeviceQueryJpaHandler implements QueryHandler<SearchDeviceQuery, Iterable<DeviceDto>> {
    private final DeviceJpaRepository deviceRepository;
    private final DeviceReadonlyJpaRepository deviceReadonlyRepository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<DeviceDto> handle(SearchDeviceQuery query) {
        Pageable pageable = query.toPageable();
        if (query.getReadonly()) {
            if (pageable.isPaged()) {
                return deviceReadonlyRepository.findAll(pageable);
            } else {
                return deviceReadonlyRepository.findAll();
            }
        } else {
            if (pageable.isPaged()) {
                return deviceRepository.findAll(pageable).map(this::mapOf);
            } else {
                return deviceRepository.findAll().stream()
                        .map(this::mapOf)
                        .collect(Collectors.toList());
            }
        }
    }

    private DeviceDto mapOf(Device device) {
        return new DeviceDto(device.deviceId(), device.deviceProperties(), device.enabled(), Collections.emptyList(), Collections.emptyList());
    }
}
