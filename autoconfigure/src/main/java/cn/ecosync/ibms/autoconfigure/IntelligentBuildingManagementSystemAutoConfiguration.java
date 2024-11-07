package cn.ecosync.ibms.autoconfigure;

import cn.ecosync.ibms.bus.DefaultCommandBus;
import cn.ecosync.ibms.bus.DefaultEventBus;
import cn.ecosync.ibms.bus.DefaultQueryBus;
import cn.ecosync.ibms.bus.OutboxEventBus;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.outbox.repository.jpa.OutboxJpaRepository;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.List;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(IntelligentBuildingManagementSystemProperties.class)
public class IntelligentBuildingManagementSystemAutoConfiguration {
    @Bean
    @ConditionalOnClass(OutboxJpaRepository.class)
    @ConditionalOnProperty(prefix = "ibms.outbox", name = "enabled")
    public EventBus outboxEventBus(OutboxJpaRepository outboxJpaRepository) {
        log.info("OutboxEventBus enabled");
        return new OutboxEventBus(outboxJpaRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBus defaultEventBus() {
        log.info("DefaultEventBus enabled");
        return new DefaultEventBus();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandBus defaultCommandBus(List<CommandHandler<?>> commandHandlers) {
        log.info("DefaultCommandBus enabled");
        return new DefaultCommandBus(commandHandlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public QueryBus defaultQueryBus(List<QueryHandler<?, ?>> queryHandlers, RestTemplateBuilder builder, Environment environment) {
        log.info("DefaultQueryBus enabled");
        return new DefaultQueryBus(queryHandlers, builder, environment);
    }
}
