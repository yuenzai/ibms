package cn.ecosync.ibms.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.Assert;

@Slf4j
public abstract class AbstractEventBus implements EventBus, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void handle(Event event) {
        Assert.notNull(applicationEventPublisher, "applicationEventPublisher is null");
        Assert.notNull(event, "event must not be null");
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
