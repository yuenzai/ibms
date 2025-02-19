package cn.ecosync.ibms.gateway.configure;

import cn.ecosync.ibms.JsonSerdeContextHolder;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.gateway.command.handler.*;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.query.handler.GetDataAcquisitionQueryHandler;
import cn.ecosync.ibms.gateway.query.handler.SearchDataAcquisitionQueryHandler;
import cn.ecosync.ibms.gateway.service.DeviceTelemetryService;
import cn.ecosync.ibms.gateway.service.GatewayMetricsTelemetryService;
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

import static cn.ecosync.ibms.Constants.PATH_METRICS;
import static cn.ecosync.ibms.Constants.PATH_METRICS_DEVICES;

@EnableScheduling
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DeviceTelemetryService.class)
public class GatewayConfiguration {
    private final GatewayMetricsTelemetryService gatewayMetricsTelemetryService;
    private final DeviceTelemetryService deviceTelemetryService;
    private final ServletRegistrationBean<PrometheusMetricsServlet> gatewayMetricsServlet;
    private final ServletRegistrationBean<PrometheusMetricsServlet> deviceMetricsServlet;

    public GatewayConfiguration() {
        PrometheusRegistry defaultRegistry = PrometheusRegistry.defaultRegistry;
        gatewayMetricsTelemetryService = new GatewayMetricsTelemetryService(defaultRegistry);
        gatewayMetricsServlet = new ServletRegistrationBean<>(new PrometheusMetricsServlet(defaultRegistry), PATH_METRICS);

        PrometheusRegistry deviceMetricsRegistry = new PrometheusRegistry();
        deviceTelemetryService = new DeviceTelemetryService(deviceMetricsRegistry);
        deviceMetricsServlet = new ServletRegistrationBean<>(new PrometheusMetricsServlet(deviceMetricsRegistry), PATH_METRICS_DEVICES);
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
    public GatewayMetricsTelemetryService gatewayMetricsTelemetryService() {
        return gatewayMetricsTelemetryService;
    }

    @Bean
    public DeviceTelemetryService deviceTelemetryService() {
        return deviceTelemetryService;
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
    public ReloadTelemetryServiceCommandHandler reloadTelemetryServiceCommandHandler(
            GatewayMetricsTelemetryService gatewayMetricsTelemetryService,
            DeviceTelemetryService deviceTelemetryService,
            DeviceDataAcquisitionRepository dataAcquisitionRepository,
            Environment environment) {
        return new ReloadTelemetryServiceCommandHandler(
                gatewayMetricsTelemetryService,
                deviceTelemetryService,
                dataAcquisitionRepository,
                environment
        );
    }

    @Bean
    public ImportBacnetDataPointsCommandHandler importBacnetDataPointsCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        return new ImportBacnetDataPointsCommandHandler(dataAcquisitionRepository, eventBus);
    }

    @Bean
    public ImportDeviceInfosCommandHandler importDeviceInfosCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        return new ImportDeviceInfosCommandHandler(dataAcquisitionRepository, eventBus);
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
