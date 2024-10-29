package cn.ecosync.ibms.outbox.repository.jpa;

import cn.ecosync.ibms.event.EventAcknowledgment;
import cn.ecosync.ibms.outbox.model.OutboxProcessed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxProcessedJpaRepository extends JpaRepository<OutboxProcessed, Integer>, EventAcknowledgment {
    @Override
    default void acknowledge(String eventId) {
        save(new OutboxProcessed(eventId));
    }
}
