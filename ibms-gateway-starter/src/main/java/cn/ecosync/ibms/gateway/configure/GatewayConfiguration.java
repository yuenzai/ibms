package cn.ecosync.ibms.gateway.configure;

import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.gateway.bacnet.BacnetService;
import cn.ecosync.ibms.gateway.command.handler.*;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.query.handler.GetDataAcquisitionQueryHandler;
import cn.ecosync.ibms.gateway.query.handler.SearchDataAcquisitionQueryHandler;
import cn.ecosync.ibms.gateway.service.DeviceTelemetryService;
import cn.ecosync.ibms.gateway.service.GatewayApplicationService;
import io.prometheus.metrics.exporter.servlet.jakarta.PrometheusMetricsServlet;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import static cn.ecosync.ibms.Constants.PATH_METRICS_DEVICES;
import static cn.ecosync.ibms.Constants.PATH_METRICS_JVM;

@EnableScheduling
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DeviceTelemetryService.class)
public class GatewayConfiguration {
    private final DeviceTelemetryService deviceTelemetryService;
    private final BacnetService bacnetService;
    private final ServletRegistrationBean<PrometheusMetricsServlet> deviceMetricsServlet;

    public GatewayConfiguration() {
        PrometheusRegistry deviceMetricsRegistry = new PrometheusRegistry();
        bacnetService = new BacnetService();
        deviceTelemetryService = new DeviceTelemetryService(deviceMetricsRegistry, bacnetService);
        deviceMetricsServlet = new ServletRegistrationBean<>(new PrometheusMetricsServlet(deviceMetricsRegistry), PATH_METRICS_DEVICES);
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(JvmMetrics.class)
    public static class JvmMetricsConfiguration {
        @Bean
        public ServletRegistrationBean<PrometheusMetricsServlet> jvmMetricsServlet() {
            PrometheusRegistry jvmMetricsRegistry = new PrometheusRegistry();
            JvmMetrics.builder().register(jvmMetricsRegistry);
            return new ServletRegistrationBean<>(new PrometheusMetricsServlet(jvmMetricsRegistry), PATH_METRICS_JVM);
        }
    }

    @Bean
    public ServletRegistrationBean<PrometheusMetricsServlet> deviceMetricsServlet() {
        return deviceMetricsServlet;
    }

    @Bean
    public BacnetService bacnetService() {
        return bacnetService;
    }

    @Bean
    public DeviceTelemetryService deviceTelemetryService() {
        return deviceTelemetryService;
    }

//    @Bean
//    public FilterRegistrationBean<Filter> jsonSerdeFilter(JsonSerde jsonSerde) {
//        Filter filter = (servletRequest, servletResponse, filterChain) -> {
//            JsonSerdeContextHolder.set(jsonSerde);
//            filterChain.doFilter(servletRequest, servletResponse);
//            JsonSerdeContextHolder.clear();
//        };
//        return new FilterRegistrationBean<>(filter, deviceMetricsServlet);
//    }

    @Bean
    public GatewayApplicationService gatewayApplicationService(DeviceTelemetryService deviceTelemetryService, DeviceDataAcquisitionRepository dataAcquisitionRepository, Environment environment) {
        return new GatewayApplicationService(deviceTelemetryService, dataAcquisitionRepository, environment);
    }

    @Bean
    public ReloadTelemetryServiceCommandHandler reloadTelemetryServiceCommandHandler(GatewayApplicationService gatewayApplicationService) {
        return new ReloadTelemetryServiceCommandHandler(gatewayApplicationService);
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
