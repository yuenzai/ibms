package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class GatewaySynchronizationService implements ApplicationRunner {
    private final GatewayService gatewayService;
    private final GatewayTelemetryService gatewayTelemetryService;
    private final GatewayConfigurationProperties gatewayProperties;
    private final TaskScheduler taskScheduler;

    public GatewaySynchronizationService(
            RestClient.Builder restClientBuilder,
            GatewayTelemetryService gatewayTelemetryService,
            GatewayConfigurationProperties gatewayProperties,
            TaskScheduler taskScheduler) {
        RestClient restClient = restClientBuilder.baseUrl("http://" + gatewayProperties.getIbmsHost()).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.gatewayService = factory.createClient(GatewayService.class);
        this.gatewayTelemetryService = gatewayTelemetryService;
        this.gatewayProperties = gatewayProperties;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void run(ApplicationArguments args) {
        scheduleSynchronize();
    }

    private void scheduleSynchronize() {
        taskScheduler.schedule(this::synchronize, Instant.now());
    }

    private void synchronize() {
        CompletableFuture.supplyAsync(this::doRequest)
                .thenAccept(this::handle)
                .whenComplete((v, e) -> {
                    if (e != null) log.error("", e);
                    scheduleSynchronize();
                });
    }

    private ResponseEntity<DeviceGateway> doRequest() {
        return gatewayService.get(gatewayProperties.getGatewayCode());
    }

    private void handle(ResponseEntity<DeviceGateway> responseEntity) {
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        if (statusCode.isSameCodeAs(HttpStatus.NO_CONTENT)) return;
        if (statusCode.isSameCodeAs(HttpStatus.OK)) {
            DeviceGateway gateway = gatewayTelemetryService.saveAndGet(responseEntity.getBody());
            taskScheduler.schedule(() -> gatewayTelemetryService.observeMeasurements(gateway), Instant.now());
            gatewayService.get(gatewayProperties.getGatewayCode(), SynchronizationStateEnum.SYNCHRONIZED.toString());
            return;
        }
        log.error("", new ResponseStatusException(statusCode, responseEntity.toString()));
    }
}
