package cn.ecosync.ibms.scheduling.event;

import cn.ecosync.ibms.Constants;
import cn.ecosync.ibms.scheduling.dto.SchedulingTaskParams;
import cn.ecosync.ibms.scheduling.dto.SchedulingTrigger;
import cn.ecosync.iframework.event.AbstractEvent;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class SchedulingRescheduledEvent extends AbstractEvent {
    public static final String TYPE = "scheduling-rescheduled";

    @NotBlank
    private final String schedulingName;
    private final SchedulingTrigger schedulingTrigger;
    private final SchedulingTaskParams schedulingTaskParams;

    @Override
    public String aggregateType() {
        return Constants.AGGREGATE_TYPE_SCHEDULING;
    }

    @Override
    public String aggregateId() {
        return schedulingName;
    }

    @Override
    public String eventType() {
        return TYPE;
    }
}
