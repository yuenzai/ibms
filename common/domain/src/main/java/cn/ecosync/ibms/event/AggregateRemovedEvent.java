package cn.ecosync.ibms.event;

import lombok.ToString;

@ToString
public class AggregateRemovedEvent extends AbstractEvent {
    private final transient String aggregateType;
    private final transient String aggregateId;

    public AggregateRemovedEvent(String aggregateType, String aggregateId) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
    }

    @Override
    public String aggregateType() {
        return aggregateType;
    }

    @Override
    public String aggregateId() {
        return aggregateId;
    }

    @Override
    public String eventType() {
        return "";
    }
}
