package cn.ecosync.ibms.scheduling.repository.jpa;

import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.model.*;
import cn.ecosync.ibms.scheduling.repository.SchedulingQueryModelExtension;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SchedulingQueryModelExtensionImpl implements SchedulingQueryModelExtension {
    private final SchedulingApplicationService schedulingApplicationService;

    @Override
    public SchedulingQueryModel toQueryModel(SchedulingModel schedulingModel) {
        SchedulingId schedulingId = schedulingModel.getSchedulingId();
        SchedulingState schedulingState = schedulingApplicationService.getSchedulingState(schedulingId);
        List<Long> nextFireTimes = null;
        Long previousFireTime = null;

        SchedulingTrigger trigger = schedulingModel.getSchedulingTrigger();
        if (trigger != null) {
            // 计算之后几次的触发时间
            nextFireTimes = schedulingApplicationService.computeNextFireTimes(schedulingId, 5).stream()
                    .map(Date::getTime)
                    .collect(Collectors.toList());
            // 获取上一次触发时间
            previousFireTime = schedulingApplicationService.getPreviousFireTime(schedulingId)
                    .map(Date::getTime)
                    .orElse(null);
        }
        return new SchedulingQueryModelDto(schedulingModel, schedulingState, nextFireTimes, previousFireTime);
    }
}
