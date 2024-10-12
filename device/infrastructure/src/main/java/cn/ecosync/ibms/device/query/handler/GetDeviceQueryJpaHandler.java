package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.DeviceMapper;
import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.device.repository.DeviceRepository;
import cn.ecosync.ibms.device.repository.jpa.DeviceReadonlyJpaRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnClass(EntityManager.class)
public class GetDeviceQueryJpaHandler implements QueryHandler<GetDeviceQuery, Optional<DeviceDto>> {
    private final DeviceRepository deviceRepository;
    private final DeviceReadonlyJpaRepository deviceReadonlyRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceDto> handle(GetDeviceQuery query) {
        if (query.getReadonly()) {
            return deviceReadonlyRepository.findByDeviceId(query.getDeviceId());
        } else {
            return deviceRepository.get(query.getDeviceId())
                    .map(DeviceMapper::mapOf);
        }
    }
}
