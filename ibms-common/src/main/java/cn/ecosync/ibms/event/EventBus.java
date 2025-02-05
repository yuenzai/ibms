package cn.ecosync.ibms.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @author yuenzai
 * @since 2024
 */
public class EventBus implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher eventPublisher;

    public void publish(Event event) {
        if (event == null) return;
        eventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
