package cn.ecosync.ibms.event;

import cn.ecosync.ibms.outbox.model.Outbox;
import cn.ecosync.ibms.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(name = {"org.springframework.data.jpa.repository.JpaRepository"})
public class EventBusJpaAdapter implements EventBus, ApplicationEventPublisherAware {
    private final OutboxRepository outboxRepository;
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(Event event) {
        Outbox outbox = new Outbox(event);
        outboxRepository.put(outbox);
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
