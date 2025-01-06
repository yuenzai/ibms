package cn.ecosync.ibms.gateway;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ToString
@ConfigurationProperties("gateway")
public class GatewayConfigurationProperties {
    private String ibmsHost;
    private String gatewayCode;
}
