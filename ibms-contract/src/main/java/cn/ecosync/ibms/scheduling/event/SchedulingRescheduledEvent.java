package cn.ecosync.ibms.scheduling.event;

import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SchedulingRescheduledEvent extends AbstractSchedulingEvent {
    public static final String EVENT_TYPE = "scheduling-rescheduled-event";

    private SchedulingId schedulingId;
    private SchedulingTrigger schedulingTrigger;
    private SchedulingTaskParams schedulingTaskParams;

    protected SchedulingRescheduledEvent() {
    }

    public SchedulingRescheduledEvent(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, SchedulingTaskParams schedulingTaskParams) {
        this.schedulingId = schedulingId;
        this.schedulingTrigger = schedulingTrigger;
        this.schedulingTaskParams = schedulingTaskParams;
    }

    @Override
    public String eventKey() {
        return schedulingId.toStringId();
    }
}
