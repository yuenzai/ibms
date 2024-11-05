package cn.ecosync.ibms.outbox;

import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.outbox.repository.jpa.OutboxJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OutboxConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "outbox", name = "enabled")
    public EventBus outboxEventBus(OutboxJpaRepository outboxJpaRepository) {
        log.info("OutboxEventBus enabled");
        return new OutboxEventBus(outboxJpaRepository);
    }
}
