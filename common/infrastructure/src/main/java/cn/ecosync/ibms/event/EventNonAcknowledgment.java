package cn.ecosync.ibms.event;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(EventAcknowledgment.class)
public class EventNonAcknowledgment implements EventAcknowledgment {
    @Override
    public void acknowledge(String eventId) {
        // no op
    }
}
