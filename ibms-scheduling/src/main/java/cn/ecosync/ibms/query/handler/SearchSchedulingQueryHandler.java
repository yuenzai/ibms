package cn.ecosync.ibms.query.handler;

import cn.ecosync.ibms.domain.SchedulingApplicationService;
import cn.ecosync.ibms.domain.SchedulingId;
import cn.ecosync.ibms.domain.SchedulingReadonlyRepository;
import cn.ecosync.ibms.dto.SchedulingDto;
import cn.ecosync.ibms.dto.SchedulingTrigger;
import cn.ecosync.ibms.query.SearchSchedulingQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchSchedulingQueryHandler implements QueryHandler<SearchSchedulingQuery, Iterable<SchedulingDto>> {
    private final SchedulingReadonlyRepository schedulingReadonlyRepository;
    private final SchedulingApplicationService schedulingApplicationService;

    @Override
    @Transactional(readOnly = true)
    public Iterable<SchedulingDto> handle(SearchSchedulingQuery query) {
        Pageable pageable = query.toPageable();
        Iterable<SchedulingDto> schedules = schedulingReadonlyRepository.search(pageable);
        putStateFor(schedules, query);
        return schedules;
    }

    private void putStateFor(Iterable<SchedulingDto> result, SearchSchedulingQuery query) {
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
