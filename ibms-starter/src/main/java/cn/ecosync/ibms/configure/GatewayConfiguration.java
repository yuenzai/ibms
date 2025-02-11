package cn.ecosync.ibms.configure;

import cn.ecosync.ibms.JsonSerdeContextHolder;
import cn.ecosync.ibms.gateway.GatewaySynchronizationService;
import cn.ecosync.ibms.gateway.PrometheusTelemetryService;
import cn.ecosync.ibms.serde.JsonSerde;
import io.prometheus.metrics.exporter.servlet.jakarta.PrometheusMetricsServlet;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@EnableScheduling
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(PrometheusTelemetryService.class)
public class GatewayConfiguration {
    public static final String PATH_METRICS = "/metrics";
    public static final String PATH_METRICS_DEVICES = "/metrics/devices";

    private final PrometheusTelemetryService prometheusTelemetryService;
    private final ServletRegistrationBean<PrometheusMetricsServlet> gatewayMetricsServlet;
    private final ServletRegistrationBean<PrometheusMetricsServlet> deviceMetricsServlet;

    public GatewayConfiguration() {
        PrometheusRegistry deviceMetricsRegistry = new PrometheusRegistry();
        prometheusTelemetryService = new PrometheusTelemetryService();
        gatewayMetricsServlet = new ServletRegistrationBean<>(new PrometheusMetricsServlet(), PATH_METRICS + "/*");
        deviceMetricsServlet = new ServletRegistrationBean<>(new PrometheusMetricsServlet(deviceMetricsRegistry), PATH_METRICS_DEVICES + "/*");
        deviceMetricsRegistry.register(prometheusTelemetryService);
    }

    @Bean
    public ServletRegistrationBean<PrometheusMetricsServlet> gatewayMetricsServlet() {
        return gatewayMetricsServlet;
    }

    @Bean
    public ServletRegistrationBean<PrometheusMetricsServlet> deviceMetricsServlet() {
        return deviceMetricsServlet;
    }

    @Bean
    @Profile("gateway")
    public GatewaySynchronizationService gatewaySynchronizationService(
            Environment environment, RestClient.Builder restClientBuilder, TaskScheduler taskScheduler) {
        return new GatewaySynchronizationService(environment, restClientBuilder, taskScheduler, prometheusTelemetryService);
    }

    @Bean
    @Profile("ibms")
    public PrometheusTelemetryService prometheusTelemetryService() {
        return prometheusTelemetryService;
    }

    @Bean
    public FilterRegistrationBean<Filter> jsonSerdeFilter(JsonSerde jsonSerde) {
        Filter filter = (servletRequest, servletResponse, filterChain) -> {
            JsonSerdeContextHolder.set(jsonSerde);
            filterChain.doFilter(servletRequest, servletResponse);
            JsonSerdeContextHolder.clear();
        };
        return new FilterRegistrationBean<>(filter, deviceMetricsServlet);
    }
}
