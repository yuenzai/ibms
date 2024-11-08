package cn.ecosync.ibms.autoconfigure;

import cn.ecosync.ibms.bus.DefaultCommandBus;
import cn.ecosync.ibms.bus.DefaultEventBus;
import cn.ecosync.ibms.bus.DefaultQueryBus;
import cn.ecosync.ibms.bus.OutboxEventBus;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.jpa.DefaultJpaService;
import cn.ecosync.ibms.jpa.JpaService;
import cn.ecosync.ibms.outbox.repository.jpa.OutboxJpaRepository;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.query.QueryHandler;
import cn.ecosync.ibms.serde.JacksonSerde;
import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.web.LoggingRequestInterceptor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cn.ecosync.ibms.Constants.*;

@AutoConfiguration
@EnableJpaAuditing
@EnableJpaRepositories("cn.ecosync.**.repository.jpa")
@EnableConfigurationProperties(IntelligentBuildingManagementSystemProperties.class)
public class IntelligentBuildingManagementSystemAutoConfiguration {
    @Bean
    @ConditionalOnClass(OutboxJpaRepository.class)
    @ConditionalOnProperty(prefix = "ibms.outbox", name = "enabled")
    public EventBus outboxEventBus(OutboxJpaRepository outboxJpaRepository) {
        return new OutboxEventBus(outboxJpaRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBus defaultEventBus() {
        return new DefaultEventBus();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandBus defaultCommandBus(List<CommandHandler<?>> commandHandlers) {
        return new DefaultCommandBus(commandHandlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public QueryBus defaultQueryBus(List<QueryHandler<?, ?>> queryHandlers, RestTemplateBuilder builder, Environment environment) {
        return new DefaultQueryBus(queryHandlers, builder, environment);
    }

    @Bean
    @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class, JPAQueryFactory.class})
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    @ConditionalOnClass(ObjectMapper.class)
    public JsonSerde jsonSerde(ObjectMapper objectMapper) {
        return new JacksonSerde(objectMapper);
    }

    @Bean
    @ConditionalOnBean(JsonSerde.class)
    public Module javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        return javaTimeModule;
    }

    @Bean
    public JpaService jpaService() {
        return new DefaultJpaService();
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
        return configurer.configure(new RestTemplateBuilder())
                .additionalInterceptors(new LoggingRequestInterceptor())
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    }
}
