package cn.ecosync.aiot.edge.gateway.configure;

import cn.ecosync.aiot.command.CommandBus;
import cn.ecosync.aiot.command.CommandHandler;
import cn.ecosync.aiot.query.QueryBus;
import cn.ecosync.aiot.query.QueryHandler;
import cn.ecosync.aiot.serde.JsonSerde;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

@AutoConfiguration
@Import({JpaConfiguration.class, GatewayConfiguration.class})
public class AIoTEdgeGatewayAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(JsonSerde.class)
    public JsonSerde jsonSerde(ObjectMapper objectMapper) {
        return new JsonSerde(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(CommandBus.class)
    public CommandBus commandBus(List<CommandHandler<?>> commandHandlers) {
        return new CommandBus(commandHandlers);
    }

    @Bean
    @ConditionalOnMissingBean(QueryBus.class)
    public QueryBus queryBus(List<QueryHandler<?, ?>> queryHandlers) {
        return new QueryBus(queryHandlers);
    }
}
