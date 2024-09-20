package cn.ecosync.ibms.event;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnMissingClass({"org.springframework.data.jpa.repository.JpaRepository"})
public class EventNonAcknowledgment implements EventAcknowledgment {
    @Override
    public void acknowledge(String eventId) {
        // no op
    }
}
