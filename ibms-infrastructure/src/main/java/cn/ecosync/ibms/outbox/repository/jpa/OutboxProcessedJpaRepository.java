package cn.ecosync.ibms.outbox.repository.jpa;

import cn.ecosync.ibms.event.EventAcknowledgment;
import cn.ecosync.ibms.outbox.model.OutboxProcessed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;

public interface OutboxProcessedJpaRepository extends JpaRepository<OutboxProcessed, Integer>, EventAcknowledgment {
    @Override
    default void acknowledge(String eventId) {
        Assert.hasText(eventId, "eventId can not be empty");
        save(new OutboxProcessed(eventId));
    }
}
