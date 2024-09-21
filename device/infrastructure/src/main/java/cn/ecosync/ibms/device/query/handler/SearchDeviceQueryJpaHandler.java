package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.ibms.device.model.QDevice;
import cn.ecosync.ibms.device.query.SearchDeviceQuery;
import cn.ecosync.ibms.query.QueryHandler;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnClass(JPAQueryFactory.class)
public class SearchDeviceQueryJpaHandler implements QueryHandler<SearchDeviceQuery, Iterable<DeviceDto>> {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional(readOnly = true)
    public Iterable<DeviceDto> handle(SearchDeviceQuery query) {
        QDevice dev = QDevice.device;
        Expression<DeviceDto> selectExpression = Projections.fields(
                DeviceDto.class,
                dev.deviceId.deviceCode,
                dev.networkId.dictKey.as("networkId"),
                dev.deviceName,
                dev.path,
                dev.description,
                dev.enabled,
                dev.deviceProperties);
        JPAQuery<?> jpaQuery = jpaQueryFactory.from(dev);
        Pageable pageable = query.toPageable();
        if (pageable.isPaged()) {
            Long count = jpaQuery.select(Wildcard.count)
                    .fetchOne();
            List<DeviceDto> result = jpaQuery.select(selectExpression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            return new PageImpl<>(result, pageable, Optional.ofNullable(count).orElse(0L));
        } else {
            return jpaQuery.select(selectExpression)
                    .fetch();
        }
    }
}
