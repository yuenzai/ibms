package cn.ecosync.ibms.autoconfigure.scheduling;

import cn.ecosync.ibms.domain.Scheduling;
import cn.ecosync.ibms.repository.jdbc.SchedulingReadonlyJdbcRepository;
import cn.ecosync.iframework.serde.JsonSerde;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@AutoConfiguration
@ConditionalOnClass(Scheduling.class)
public class SchedulingAutoConfiguration {
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Scheduling.class, JdbcTemplate.class})
    public static class SchedulingJdbcConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public SchedulingReadonlyJdbcRepository schedulingReadonlyRepository(NamedParameterJdbcTemplate jdbcTemplate, JsonSerde jsonSerde) {
            return new SchedulingReadonlyJdbcRepository(jdbcTemplate, jsonSerde);
        }
    }
}
