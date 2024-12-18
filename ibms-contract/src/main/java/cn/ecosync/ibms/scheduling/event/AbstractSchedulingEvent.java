package cn.ecosync.ibms.scheduling.event;

import cn.ecosync.iframework.event.AbstractEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static cn.ecosync.ibms.Constants.AGGREGATE_TYPE_SCHEDULING;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SchedulingRescheduledEvent.class, name = SchedulingRescheduledEvent.EVENT_TYPE),
})
public abstract class AbstractSchedulingEvent extends AbstractEvent {
    public AbstractSchedulingEvent() {
        super(AGGREGATE_TYPE_SCHEDULING);
    }
}
