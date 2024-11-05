package cn.ecosync.ibms;

import cn.ecosync.ibms.bus.DefaultCommandBus;
import cn.ecosync.ibms.bus.DefaultEventBus;
import cn.ecosync.ibms.bus.DefaultQueryBus;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Slf4j
@Configuration
public class IntelligentBuildingManagementSystemConfiguration {
    @Bean
    public EventBus defaultEventBus() {
        log.info("DefaultEventBus enabled");
        return new DefaultEventBus();
    }

    @Bean
    public CommandBus defaultCommandBus(List<CommandHandler<?>> commandHandlers) {
        log.info("DefaultCommandBus enabled");
        return new DefaultCommandBus(commandHandlers);
    }

    @Bean
    public QueryBus defaultQueryBus(List<QueryHandler<?, ?>> queryHandlers, RestTemplateBuilder builder, Environment environment) {
        log.info("DefaultQueryBus enabled");
        return new DefaultQueryBus(queryHandlers, builder, environment);
    }
}
