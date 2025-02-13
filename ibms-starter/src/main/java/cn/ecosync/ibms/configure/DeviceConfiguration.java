package cn.ecosync.ibms.configure;

import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.device.command.handler.RemoveDataAcquisitionCommandHandler;
import cn.ecosync.ibms.device.command.handler.SaveDataAcquisitionCommandHandler;
import cn.ecosync.ibms.device.controller.DeviceDataAcquisitionWebController;
import cn.ecosync.ibms.device.controller.TelemetryWebController;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.device.model.TelemetryService;
import cn.ecosync.ibms.device.query.handler.GetDataAcquisitionQueryHandler;
import cn.ecosync.ibms.device.query.handler.SearchDataAcquisitionQueryHandler;
import cn.ecosync.ibms.device.service.PrometheusService;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.query.QueryBus;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DeviceDataAcquisitionWebController.class)
public class DeviceConfiguration {
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

    @Bean
    public PrometheusService prometheusService(RestClient.Builder restClientBuilder, Environment environment) {
        String prometheusEndpoint = environment.getProperty("PROMETHEUS_ENDPOINT", "localhost:9090");
        RestClient restClient = restClientBuilder.baseUrl("http://" + prometheusEndpoint).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(PrometheusService.class);
    }

    @Bean
    public TelemetryWebController telemetryWebController(CommandBus commandBus, QueryBus queryBus, ObjectProvider<TelemetryService> telemetryServiceProvider) {
        return new TelemetryWebController(commandBus, queryBus, telemetryServiceProvider.getIfAvailable());
    }
}
