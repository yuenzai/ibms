package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.query.SearchDeviceListQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceJpaRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceReadonlyRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@ConditionalOnClass(EntityManager.class)
public class SearchDeviceListQueryJpaHandler implements QueryHandler<SearchDeviceListQuery, List<DeviceDto>> {
    private final DeviceJpaRepository deviceRepository;
    private final DeviceReadonlyRepository deviceReadonlyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto> handle(SearchDeviceListQuery query) {
        if (query.getReadonly()) {
            return deviceReadonlyRepository.findAll();
        } else {
            return deviceRepository.findAll().stream()
                    .map(DeviceMapper::map)
                    .collect(Collectors.toList());
        }
    }
}
