package cn.ecosync.ibms.bus;

import cn.ecosync.ibms.event.AbstractEventBus;
import cn.ecosync.ibms.event.AggregateRemovedEvent;
import cn.ecosync.ibms.event.Event;
import cn.ecosync.ibms.outbox.model.Outbox;
import cn.ecosync.ibms.outbox.repository.jpa.OutboxJpaRepository;
import cn.ecosync.ibms.serde.JsonSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author yuenzai
 * @since 2024
 */
@Slf4j
@RequiredArgsConstructor
public class OutboxEventBus extends AbstractEventBus {
    private final OutboxJpaRepository outboxJpaRepository;
    private final JsonSerde jsonSerde;

    @Override
    public void publish(Event event) {
        Assert.notNull(event, "event must not be null");
        Outbox outbox = toOutbox(event);
        outboxJpaRepository.save(outbox);
    }

    public Outbox toOutbox(Event event) {
        String payload = Optional.of(event)
                .filter(in -> !(in instanceof AggregateRemovedEvent))
                .flatMap(jsonSerde::writeValueAsString)
                .orElse(null);
        return new Outbox(
                event.aggregateType(),
                event.aggregateId(),
                event.eventId(),
                event.eventTime(),
                event.eventType(),
                payload
        );
    }
}
