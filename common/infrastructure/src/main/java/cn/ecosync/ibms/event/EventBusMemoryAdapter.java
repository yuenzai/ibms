package cn.ecosync.ibms.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * @author 覃俊元
 * @since 2024
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnMissingClass({"org.springframework.data.jpa.repository.JpaRepository"})
public class EventBusMemoryAdapter implements EventBus, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(Event event) {
        handleImpl(event);
    }

    @Override
    public void handle(Event event) {
        handleImpl(event);
    }

    private void handleImpl(Event event) {
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
