package cn.ecosync.ibms.repository;

import cn.ecosync.ibms.outbox.model.Outbox;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface OutboxRepository {
    void put(Outbox outbox);
}
