package cn.ecosync.ibms.event;

import lombok.ToString;
import org.springframework.util.Assert;

@ToString
public class AggregateRemovedEvent extends AbstractEvent {
    private final String aggregateType;
    private final String aggregateId;

    public AggregateRemovedEvent(String aggregateType, String aggregateId) {
        Assert.hasText(aggregateType, "aggregateType can not be null");
        Assert.hasText(aggregateId, "aggregateId can not be null");
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
