package cn.ecosync.ibms.event;

import cn.ecosync.ibms.model.AggregateRoot;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;
import org.springframework.util.Assert;

@ToString
public class AggregateSavedEvent extends AbstractEvent {
    @JsonUnwrapped
    private final AggregateRoot aggregateRoot;

    public AggregateSavedEvent(AggregateRoot aggregateRoot) {
        Assert.notNull(aggregateRoot, "aggregateRoot can not be null");
        this.aggregateRoot = aggregateRoot;
    }

    @Override
    public String aggregateType() {
        return aggregateRoot.aggregateType();
    }

    @Override
    public String aggregateId() {
        return aggregateRoot.aggregateId();
    }

    @Override
    public String eventType() {
        return "";
    }

    public AggregateRoot aggregateRoot() {
        return aggregateRoot;
    }
}
