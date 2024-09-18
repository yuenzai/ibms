package cn.ecosync.ibms.event;

import java.io.Serializable;
import java.time.Instant;

public interface Event extends Serializable {
    String aggregateType();

    String aggregateId();

    String eventId();

    Instant eventTime();

    String eventType();
}
