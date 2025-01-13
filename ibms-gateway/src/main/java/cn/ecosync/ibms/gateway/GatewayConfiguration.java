package cn.ecosync.ibms.gateway;

import cn.ecosync.iframework.serde.JsonSerde;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@Configuration
@EnableScheduling
public class GatewayConfiguration {
    @Bean
    public OpenTelemetry openTelemetry() {
        return AutoConfiguredOpenTelemetrySdk.builder()
                .build()
                .getOpenTelemetrySdk();
    }

    @Bean
    public GatewayTelemetryService gatewayTelemetryService(OpenTelemetry openTelemetry, JsonSerde jsonSerde) {
        return new GatewayTelemetryService(openTelemetry, jsonSerde);
    }

    @Bean
    public GatewaySynchronizationService gatewaySynchronizationService(
            Environment environment,
            RestClient.Builder restClientBuilder,
            GatewayTelemetryService gatewayTelemetryService,
            TaskScheduler taskScheduler) {
        return new GatewaySynchronizationService(environment, restClientBuilder, gatewayTelemetryService, taskScheduler);
    }
}
