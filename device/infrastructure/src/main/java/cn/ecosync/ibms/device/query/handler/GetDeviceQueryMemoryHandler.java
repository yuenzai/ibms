package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.query.QueryHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnMissingClass({"org.springframework.data.jpa.repository.JpaRepository"})
public class GetDeviceQueryMemoryHandler implements QueryHandler<GetDeviceQuery, Optional<DeviceDto>> {
    @Override
    public Optional<DeviceDto> handle(GetDeviceQuery query) {
        return Optional.empty();
    }
}
