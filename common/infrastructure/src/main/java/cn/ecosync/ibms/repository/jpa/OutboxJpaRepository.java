package cn.ecosync.ibms.repository.jpa;

import cn.ecosync.ibms.outbox.model.Outbox;
import cn.ecosync.ibms.repository.OutboxRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxJpaRepository extends JpaRepository<Outbox, Integer>, OutboxRepository {
    @Override
    default void put(Outbox outbox) {
        save(outbox);
    }
}
