package cn.ecosync.ibms.scheduling.event.handler;

import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.event.SchedulingRescheduledEvent;
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
        schedulingApplicationService.schedule(event.getSchedulingId(), event.getSchedulingTrigger(), event.getSchedulingTaskParams());
    }
}
