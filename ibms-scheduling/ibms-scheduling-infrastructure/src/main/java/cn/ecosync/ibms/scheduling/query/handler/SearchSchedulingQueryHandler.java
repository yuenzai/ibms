package cn.ecosync.ibms.scheduling.query.handler;

import cn.ecosync.ibms.query.QueryHandler;
import cn.ecosync.ibms.scheduling.application.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.dto.SchedulingDto;
import cn.ecosync.ibms.scheduling.model.QScheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import cn.ecosync.ibms.scheduling.query.SearchSchedulingQuery;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchSchedulingQueryHandler implements QueryHandler<SearchSchedulingQuery, Iterable<SchedulingDto>> {
    private final JPAQueryFactory jpaQueryFactory;
    private final SchedulingApplicationService schedulingApplicationService;

    @Override
    @Transactional(readOnly = true)
    public Iterable<SchedulingDto> handle(SearchSchedulingQuery query) {
        QScheduling scheduling = QScheduling.scheduling;
        Expression<SchedulingDto> selectExpression = Projections.fields(
                SchedulingDto.class,
                scheduling.schedulingId.schedulingName,
                scheduling.schedulingTrigger,
                scheduling.schedulingTaskParams,
                scheduling.description,
                scheduling.createdDate,
                scheduling.lastModifiedDate
        );
        JPAQuery<?> jpaQuery = jpaQueryFactory.from(scheduling);
        Pageable pageable = query.toPageable();
        if (pageable.isPaged()) {
            Long count = jpaQuery.select(Wildcard.count)
                    .fetchOne();
            List<SchedulingDto> result = jpaQuery.select(selectExpression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(scheduling.id.desc())
                    .fetch();
            putStateFor(result, query);
            return new PageImpl<>(result, pageable, Optional.ofNullable(count).orElse(0L));
        } else {
            List<SchedulingDto> result = jpaQuery.select(selectExpression)
                    .orderBy(scheduling.id.desc())
                    .fetch();
            putStateFor(result, query);
            return result;
        }
    }

    private void putStateFor(List<SchedulingDto> result, SearchSchedulingQuery query) {
        for (SchedulingDto schedulingStatusDto : result) {
            SchedulingId schedulingId = new SchedulingId(schedulingStatusDto.getSchedulingName());
            schedulingStatusDto.setSchedulingState(schedulingApplicationService.getSchedulingState(schedulingId));

            SchedulingTrigger trigger = schedulingStatusDto.getSchedulingTrigger();
            if (trigger != null) {
                // 计算之后几次的触发时间
                List<Long> nextFireTimes = schedulingApplicationService.computeNextFireTimes(schedulingId, query.getMaxCount()).stream()
                        .map(Date::getTime)
                        .collect(Collectors.toList());
                schedulingStatusDto.setNextFireTimes(nextFireTimes);
                // 获取上一次触发时间
                schedulingApplicationService.getPreviousFireTime(schedulingId)
                        .map(Date::getTime)
                        .ifPresent(schedulingStatusDto::setPreviousFireTime);
            }
        }
    }
}
