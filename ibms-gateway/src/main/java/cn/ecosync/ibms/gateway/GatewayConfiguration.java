package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.JsonSerdeContextHolder;
import cn.ecosync.iframework.serde.JsonSerde;
import io.prometheus.metrics.exporter.servlet.jakarta.PrometheusMetricsServlet;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
    private final JsonSerde jsonSerde;

    public GatewayConfiguration(JsonSerde jsonSerde) {
        this.jsonSerde = jsonSerde;
    }

    @Bean
    public PrometheusRegistry deviceMetricsRegistry() {
        return new PrometheusRegistry();
    }

    @Bean
    public ServletRegistrationBean<PrometheusMetricsServlet> gatewayMetricsServlet() {
        return new ServletRegistrationBean<>(new PrometheusMetricsServlet(), PATH_METRICS + "/*");
    }

    @Bean
    public ServletRegistrationBean<PrometheusMetricsServlet> deviceMetricsServlet() {
        return new ServletRegistrationBean<>(new PrometheusMetricsServlet(deviceMetricsRegistry()), PATH_METRICS_DEVICES + "/*");
    }

    @Bean
    public PrometheusTelemetryService prometheusTelemetryService(
            Environment environment, RestClient.Builder restClientBuilder, PrometheusRegistry deviceMetricsRegistry) {
        return new PrometheusTelemetryService(environment, restClientBuilder, deviceMetricsRegistry);
    }

    @Bean
    public GatewaySynchronizationService gatewaySynchronizationService(
            Environment environment, RestClient.Builder restClientBuilder,
            TaskScheduler taskScheduler, PrometheusTelemetryService telemetryService) {
        return new GatewaySynchronizationService(environment, restClientBuilder, taskScheduler, telemetryService);
    }

    @Bean
    public FilterRegistrationBean<Filter> jsonSerdeFilter() {
        Filter filter = (servletRequest, servletResponse, filterChain) -> {
            JsonSerdeContextHolder.set(jsonSerde);
            filterChain.doFilter(servletRequest, servletResponse);
            JsonSerdeContextHolder.clear();
        };
        return new FilterRegistrationBean<>(filter, deviceMetricsServlet());
    }
}
