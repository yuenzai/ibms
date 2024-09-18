package cn.ecosync.ibms.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties("ibms.outbox")
public class OutboxConfigurationProperties {
    private Boolean enabled;
}
