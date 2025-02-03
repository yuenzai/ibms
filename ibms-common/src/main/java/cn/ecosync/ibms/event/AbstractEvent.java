package cn.ecosync.ibms.event;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * @author yuenzai
 * @since 2024
 */
public abstract class AbstractEvent implements Event {
    private String eventDestination;
    private String eventId;
    private Long eventTime;

    protected AbstractEvent() {
    }

    public AbstractEvent(String eventDestination) {
        this(eventDestination, null, null);
    }

    public AbstractEvent(String eventDestination, String eventId, Long eventTime) {
        Assert.hasText(eventDestination, "eventDestination must not be empty");
        this.eventDestination = eventDestination;
        this.eventId = eventId != null ? eventId : UUID.randomUUID().toString().replace("-", "");
        this.eventTime = eventTime != null ? eventTime : System.currentTimeMillis();
    }

    @Override
    public String eventDestination() {
        return eventDestination;
    }

    @Override
    public String eventId() {
        return eventId;
    }

    @Override
    public Long eventTime() {
        return eventTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEvent)) return false;
        AbstractEvent that = (AbstractEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventId);
    }
}
