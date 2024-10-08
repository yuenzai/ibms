package cn.ecosync.ibms.scheduling.query.handler;

import cn.ecosync.ibms.query.QueryHandler;
import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.dto.SchedulingStatusDto;
import cn.ecosync.ibms.scheduling.model.QScheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.query.SearchSchedulingQuery;
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
public class SearchSchedulingQueryHandler implements QueryHandler<SearchSchedulingQuery, Iterable<SchedulingStatusDto>> {
    private final JPAQueryFactory jpaQueryFactory;
    private final SchedulingApplicationService schedulingApplicationService;

    @Override
    @Transactional(readOnly = true)
    public Iterable<SchedulingStatusDto> handle(SearchSchedulingQuery query) {
        QScheduling scheduling = QScheduling.scheduling;
        Expression<SchedulingStatusDto> selectExpression = Projections.fields(
                SchedulingStatusDto.class,
                scheduling.schedulingId.schedulingName,
                scheduling.schedulingTrigger,
                scheduling.enabled,
                scheduling.createdDate,
                scheduling.lastModifiedDate
        );
        JPAQuery<?> jpaQuery = jpaQueryFactory.from(scheduling);
        Pageable pageable = query.toPageable();
        if (pageable.isPaged()) {
            Long count = jpaQuery.select(Wildcard.count)
                    .fetchOne();
            List<SchedulingStatusDto> result = jpaQuery.select(selectExpression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(scheduling.id.desc())
                    .fetch();
            setStatusFor(result);
            return new PageImpl<>(result, pageable, Optional.ofNullable(count).orElse(0L));
        } else {
            List<SchedulingStatusDto> result = jpaQuery.select(selectExpression)
                    .orderBy(scheduling.id.desc())
                    .fetch();
            setStatusFor(result);
            return result;
        }
    }

    private void setStatusFor(List<SchedulingStatusDto> result) {
        for (SchedulingStatusDto schedulingStatusDto : result) {
            SchedulingId schedulingId = new SchedulingId(schedulingStatusDto.getSchedulingName());
            schedulingStatusDto.setRunning(schedulingApplicationService.isRunning(schedulingId));
        }
    }
}
