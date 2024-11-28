package cn.ecosync.ibms.scheduling.query.handler;

import cn.ecosync.ibms.scheduling.domain.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.domain.SchedulingId;
import cn.ecosync.ibms.scheduling.dto.SchedulingDto;
import cn.ecosync.ibms.scheduling.dto.SchedulingTrigger;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

class SearchSchedulingQueryHandler {
    private final SchedulingApplicationService schedulingApplicationService;

    SearchSchedulingQueryHandler(SchedulingApplicationService schedulingApplicationService) {
        this.schedulingApplicationService = schedulingApplicationService;
    }

    protected void putStateFor(Iterable<SchedulingDto> result, Integer maxCount) {
        for (SchedulingDto schedulingStatusDto : result) {
            SchedulingId schedulingId = new SchedulingId(schedulingStatusDto.getSchedulingName());
            schedulingStatusDto.setSchedulingState(schedulingApplicationService.getSchedulingState(schedulingId));

            SchedulingTrigger trigger = schedulingStatusDto.getSchedulingTrigger();
            if (trigger != null) {
                // 计算之后几次的触发时间
                List<Long> nextFireTimes = schedulingApplicationService.computeNextFireTimes(schedulingId, maxCount).stream()
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
