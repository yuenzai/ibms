package cn.ecosync.ibms.outbox;

import cn.ecosync.ibms.event.AbstractEventBus;
import cn.ecosync.ibms.event.Event;
import cn.ecosync.ibms.outbox.model.Outbox;
import cn.ecosync.ibms.outbox.repository.jpa.OutboxJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @author yuenzai
 * @since 2024
 */
@Slf4j
@RequiredArgsConstructor
public class OutboxEventBus extends AbstractEventBus {
    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public void publish(Event event) {
        Assert.notNull(event, "event must not be null");
        Outbox outbox = new Outbox(event);
        outboxJpaRepository.save(outbox);
    }
}
