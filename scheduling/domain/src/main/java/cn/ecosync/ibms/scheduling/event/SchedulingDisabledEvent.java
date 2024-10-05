package cn.ecosync.ibms.scheduling.event;

import cn.ecosync.ibms.event.AbstractEvent;
import cn.ecosync.ibms.scheduling.model.SchedulingConstant;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class SchedulingDisabledEvent extends AbstractEvent {
    public static final String TYPE = "scheduling-disabled";
    private final SchedulingId schedulingId;

    @Override
    public String aggregateType() {
        return SchedulingConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return schedulingId.toString();
    }

    @Override
    public String eventType() {
        return TYPE;
    }
}
