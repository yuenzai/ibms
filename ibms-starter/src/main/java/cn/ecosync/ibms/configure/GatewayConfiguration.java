package cn.ecosync.ibms.configure;

import cn.ecosync.ibms.JsonSerdeContextHolder;
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
//    @Bean
//    public DataAcquisitionHttpService dataAcquisitionService(RestClient.Builder restClientBuilder, Environment environment) {
//        String ibmsHost = environment.getProperty("IBMS_HOST");
//        Assert.hasText(ibmsHost, "Environment variable required: IBMS_HOST");
//        RestClient restClient = restClientBuilder.baseUrl("http://" + ibmsHost).build();
//        RestClientAdapter adapter = RestClientAdapter.create(restClient);
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//        return factory.createClient(DataAcquisitionHttpService.class);
//    }
//
//    @Bean
//    @ConditionalOnBean(DataAcquisitionHttpService.class)
//    public GatewaySynchronizationService gatewaySynchronizationService(
//            DataAcquisitionHttpService dataAcquisitionService, TaskScheduler taskScheduler, Environment environment) {
//        return new GatewaySynchronizationService(dataAcquisitionService, prometheusTelemetryService, taskScheduler, environment);
//    }
}
