package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.model.QDevice;
import cn.ecosync.ibms.device.query.GetDeviceQuery;
import cn.ecosync.ibms.query.QueryHandler;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnClass(JPAQueryFactory.class)
public class GetDeviceQueryJpaHandler implements QueryHandler<GetDeviceQuery, Optional<DeviceDto>> {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<DeviceDto> handle(GetDeviceQuery query) {
        QDevice dev = QDevice.device;
        DeviceDto dto = jpaQueryFactory.select(Projections.fields(
                        DeviceDto.class,
                        dev.deviceId.deviceCode,
                        dev.networkId.dictKey.as("networkId"),
                        dev.deviceName,
                        dev.path,
                        dev.description,
                        dev.enabled,
                        dev.deviceProperties
                ))
                .from(dev)
                .where(dev.deviceId.deviceCode.eq(query.getDeviceCode()))
                .fetchOne();
        return Optional.ofNullable(dto);
    }
}
