package cn.ecosync.ibms.repository.memory;

import cn.ecosync.ibms.outbox.model.Outbox;
import cn.ecosync.ibms.repository.OutboxRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnMissingClass({"org.springframework.data.jpa.repository.JpaRepository"})
public class OutboxMemoryRepository implements OutboxRepository {
    @Override
    public void put(Outbox outbox) {

    }
}
