package cn.ecosync.ibms.scheduling.event.handler;

import cn.ecosync.ibms.scheduling.application.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.event.SchedulingRescheduledEvent;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SchedulingRescheduledEventHandler {
    private final SchedulingApplicationService schedulingApplicationService;

    @Transactional
    @EventListener
    public void onEvent(SchedulingRescheduledEvent event) {
        SchedulingId schedulingId = new SchedulingId(event.getSchedulingName());
        schedulingApplicationService.schedule(schedulingId, event.getSchedulingTrigger(), event.getSchedulingTaskParams());
    }
}
