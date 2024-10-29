package cn.ecosync.ibms.event;

import cn.ecosync.ibms.outbox.model.Outbox;
import cn.ecosync.ibms.outbox.repository.jpa.OutboxJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuenzai
 * @since 2024
 */
@Slf4j
@RequiredArgsConstructor
public class OutboxEventBus extends AbstractEventBus {
    private final OutboxJpaRepository outboxRepository;

    @Override
    public void publish(Event event) {
        Outbox outbox = new Outbox(event);
        outboxRepository.save(outbox);
    }
}
