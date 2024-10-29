package cn.ecosync.ibms.config;

import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.event.OutboxEventBus;
import cn.ecosync.ibms.event.TransactionalEventBus;
import cn.ecosync.ibms.outbox.repository.jpa.OutboxJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OutboxConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "outbox", name = "enabled")
    public EventBus outboxEventBus(OutboxJpaRepository outboxJpaRepository) {
        log.info("OutboxEventBus enabled");
        return new OutboxEventBus(outboxJpaRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBus transactionalEventBus() {
        log.info("TransactionalEventBus enabled");
        return new TransactionalEventBus();
    }
}
