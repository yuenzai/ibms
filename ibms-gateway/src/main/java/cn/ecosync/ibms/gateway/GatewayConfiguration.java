package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.bacnet.service.BacnetService;
import cn.ecosync.iframework.serde.JsonSerde;
import io.prometheus.metrics.exporter.servlet.jakarta.PrometheusMetricsServlet;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

import static cn.ecosync.ibms.gateway.PrometheusTelemetryService.PATH_METRICS;
import static cn.ecosync.ibms.gateway.PrometheusTelemetryService.PATH_METRICS_DEVICES;

@Configuration
@EnableScheduling
public class GatewayConfiguration {
    @Bean
    public ServletRegistrationBean<PrometheusMetricsServlet> gatewayMetricsPrometheusEndpoint() {
        return new ServletRegistrationBean<>(new PrometheusMetricsServlet(), PATH_METRICS + "/*");
    }

    @Bean
    public PrometheusRegistry deviceMetricsRegistry() {
        return new PrometheusRegistry();
    }

    @Bean
    public ServletRegistrationBean<PrometheusMetricsServlet> deviceMetricsPrometheusEndpoint(PrometheusRegistry deviceMetricsRegistry) {
        return new ServletRegistrationBean<>(new PrometheusMetricsServlet(deviceMetricsRegistry), PATH_METRICS_DEVICES + "/*");
    }

    @Bean
    public BacnetService bacnetService(JsonSerde jsonSerde) {
        return new BacnetService(jsonSerde);
    }

    @Bean
    public PrometheusTelemetryService prometheusTelemetryService(
            Environment environment, RestClient.Builder restClientBuilder,
            PrometheusRegistry deviceMetricsRegistry, BacnetService bacnetService) {
        return new PrometheusTelemetryService(environment, restClientBuilder, deviceMetricsRegistry, bacnetService);
    }

    @Bean
    public GatewaySynchronizationService gatewaySynchronizationService(
            Environment environment, RestClient.Builder restClientBuilder,
            TaskScheduler taskScheduler, PrometheusTelemetryService telemetryService) {
        return new GatewaySynchronizationService(environment, restClientBuilder, taskScheduler, telemetryService);
    }
}
