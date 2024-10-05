package cn.ecosync.ibms.scheduling.event.handler;

import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.event.SchedulingDisabledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulingDisabledEventHandler {
    private final SchedulingApplicationService schedulingApplicationService;

    @EventListener
    public void onEvent(SchedulingDisabledEvent event) {
        schedulingApplicationService.cancel(event.getSchedulingId());
    }
}
