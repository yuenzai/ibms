package cn.ecosync.ibms.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

@Slf4j
public abstract class AbstractEventBus implements EventBus, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void handle(Event event) {
        if (applicationEventPublisher == null || event == null) {
            log.error("No SpringEventPublisher or Event");
            return;
        }
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
