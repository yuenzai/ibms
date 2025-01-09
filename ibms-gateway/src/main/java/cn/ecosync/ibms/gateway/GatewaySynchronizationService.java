package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.model.DeviceGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
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
public class GatewaySynchronizationService implements CommandLineRunner {
    private final GatewayService gatewayService;
    private final GatewayTelemetryService gatewayTelemetryService;
    private final String GATEWAY_ID;
    private final TaskScheduler taskScheduler;

    public GatewaySynchronizationService(
            Environment environment,
            RestClient.Builder restClientBuilder,
            GatewayTelemetryService gatewayTelemetryService,
            TaskScheduler taskScheduler) {
        this.GATEWAY_ID = environment.getRequiredProperty("GATEWAY_ID");
        String IBMS_HOST = environment.getRequiredProperty("IBMS_HOST");
        RestClient restClient = restClientBuilder.baseUrl("http://" + IBMS_HOST).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.gatewayService = factory.createClient(GatewayService.class);
        this.gatewayTelemetryService = gatewayTelemetryService;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void run(String... args) {
        try {
            synchronize(true).get();
            log.info("开启定时任务");
            scheduleSynchronize();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void scheduleSynchronize() {
        Runnable task = () -> synchronize(false)
                .whenComplete((v, e) -> {
                    if (e != null) log.error("", e);
                    scheduleSynchronize();
                });
        taskScheduler.schedule(task, Instant.now());
    }

    private CompletableFuture<Void> synchronize(boolean initial) {
        return CompletableFuture.supplyAsync(() -> doRequest(initial))
                .thenAccept(this::handle);
    }

    private ResponseEntity<DeviceGateway> doRequest(boolean initial) {
        return gatewayService.get(GATEWAY_ID, initial);
    }

    private void handle(ResponseEntity<DeviceGateway> responseEntity) {
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        if (statusCode.isSameCodeAs(HttpStatus.NO_CONTENT)) return;
        if (statusCode.isSameCodeAs(HttpStatus.OK)) {
            DeviceGateway gateway = gatewayTelemetryService.saveAndGet(responseEntity.getBody());
            taskScheduler.schedule(() -> gatewayTelemetryService.observeMeasurements(gateway), Instant.now());
            log.info("通知 IBMS 已同步...");
            gatewayService.notifySynchronized(GATEWAY_ID);
            return;
        }
        log.error("", new ResponseStatusException(statusCode, responseEntity.toString()));
    }
}
