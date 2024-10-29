package cn.ecosync.ibms.outbox.repository.jpa;

import cn.ecosync.ibms.outbox.model.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxJpaRepository extends JpaRepository<Outbox, Integer> {
}
