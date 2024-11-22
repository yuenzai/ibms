package cn.ecosync.ibms.autoconfigure.device;

import cn.ecosync.ibms.domain.Device;
import cn.ecosync.ibms.repository.jdbc.DeviceReadonlyJdbcRepository;
import cn.ecosync.iframework.serde.JsonSerde;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@AutoConfiguration
@ConditionalOnClass(Device.class)
public class DeviceAutoConfiguration {
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(JdbcTemplate.class)
    public static class DeviceJdbcConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public DeviceReadonlyJdbcRepository deviceReadonlyRepository(NamedParameterJdbcTemplate jdbcTemplate, JsonSerde jsonSerde) {
            return new DeviceReadonlyJdbcRepository(jdbcTemplate, jsonSerde);
        }
    }
}
