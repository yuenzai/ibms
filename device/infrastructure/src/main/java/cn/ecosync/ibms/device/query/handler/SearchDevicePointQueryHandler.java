package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.dto.DevicePointDto;
import cn.ecosync.ibms.device.model.QDevicePoint;
import cn.ecosync.ibms.device.query.SearchDevicePointQuery;
import cn.ecosync.ibms.query.QueryHandler;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchDevicePointQueryHandler implements QueryHandler<SearchDevicePointQuery, Iterable<DevicePointDto>> {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional(readOnly = true)
    public Iterable<DevicePointDto> handle(SearchDevicePointQuery query) {
        QDevicePoint dp = QDevicePoint.devicePoint;
        Expression<DevicePointDto> expression = Projections.fields(
                DevicePointDto.class,
                dp.pointId.pointCode,
                dp.pointName,
                dp.pointProperties
        );
        return jpaQueryFactory.select(expression)
                .from(dp)
                .where(dp.pointId.deviceCode.eq(query.getDeviceCode()))
                .fetch();
    }
}
