package cn.ecosync.ibms.event;

import cn.ecosync.ibms.config.OutboxConfigurationProperties;
import cn.ecosync.ibms.outbox.model.Outbox;
import cn.ecosync.ibms.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class EventBusDefaultAdapter implements EventBus, ApplicationEventPublisherAware {
    private final OutboxRepository outboxRepository;
    private final OutboxConfigurationProperties outboxConfig;
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(Event event) {
        if (outboxConfig.getEnabled()) {
            Outbox outbox = new Outbox(event);
            outboxRepository.put(outbox);
        } else {
            handleImpl(event);
        }
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
