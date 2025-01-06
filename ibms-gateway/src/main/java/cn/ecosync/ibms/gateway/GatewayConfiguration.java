package cn.ecosync.ibms.gateway;

import cn.ecosync.iframework.serde.JsonSerde;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GatewayConfigurationProperties.class)
public class GatewayConfiguration {
    @Bean
    public OpenTelemetry openTelemetry() {
        return AutoConfiguredOpenTelemetrySdk.builder()
//                .addMeterProviderCustomizer((builder, configProperties) -> configure(builder))
                .build()
                .getOpenTelemetrySdk();
    }

    @Bean
    public GatewayTelemetryService gatewayTelemetryService(OpenTelemetry openTelemetry, JsonSerde jsonSerde) {
        return new GatewayTelemetryService(openTelemetry, jsonSerde);
    }

    @Bean
    public GatewaySynchronizationService gatewaySynchronizationService(
            RestClient.Builder restClientBuilder,
            GatewayTelemetryService gatewayTelemetryService,
            GatewayConfigurationProperties gatewayProperties,
            TaskScheduler taskScheduler) {
        return new GatewaySynchronizationService(restClientBuilder, gatewayTelemetryService, gatewayProperties, taskScheduler);
    }
}
