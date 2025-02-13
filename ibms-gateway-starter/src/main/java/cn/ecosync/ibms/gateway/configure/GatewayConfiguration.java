package cn.ecosync.ibms.gateway.configure;

import cn.ecosync.ibms.JsonSerdeContextHolder;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.gateway.command.handler.PushGatewayConfigurationCommandHandler;
import cn.ecosync.ibms.gateway.command.handler.RemoveDataAcquisitionCommandHandler;
import cn.ecosync.ibms.gateway.command.handler.SaveDataAcquisitionCommandHandler;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.query.handler.GetDataAcquisitionQueryHandler;
import cn.ecosync.ibms.gateway.query.handler.SearchDataAcquisitionQueryHandler;
import cn.ecosync.ibms.gateway.service.PrometheusTelemetryService;
import cn.ecosync.ibms.serde.JsonSerde;
import io.prometheus.metrics.exporter.servlet.jakarta.PrometheusMetricsServlet;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

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

    @Bean
    public PushGatewayConfigurationCommandHandler pushGatewayConfigurationCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, Environment environment) {
        return new PushGatewayConfigurationCommandHandler(dataAcquisitionRepository, environment);
    }

    @Bean
    public SaveDataAcquisitionCommandHandler saveDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        return new SaveDataAcquisitionCommandHandler(dataAcquisitionRepository, eventBus);
    }

    @Bean
    public RemoveDataAcquisitionCommandHandler removeDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        return new RemoveDataAcquisitionCommandHandler(dataAcquisitionRepository, eventBus);
    }

    @Bean
    public GetDataAcquisitionQueryHandler getDataAcquisitionQueryHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        return new GetDataAcquisitionQueryHandler(dataAcquisitionRepository);
    }

    @Bean
    public SearchDataAcquisitionQueryHandler searchDataAcquisitionQueryHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        return new SearchDataAcquisitionQueryHandler(dataAcquisitionRepository);
    }
}
