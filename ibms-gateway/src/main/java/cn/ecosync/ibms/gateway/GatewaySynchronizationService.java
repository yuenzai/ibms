package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.command.SetGatewaySynchronizationStateCommand;
import cn.ecosync.ibms.device.model.DeviceGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum.SYNCHRONIZED;

public class GatewaySynchronizationService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(GatewaySynchronizationService.class);

    private final GatewayService gatewayService;
    private final String GATEWAY_ID;
    private final TaskScheduler taskScheduler;
    private final PrometheusTelemetryService telemetryService;

    public GatewaySynchronizationService(
            Environment environment,
            RestClient.Builder restClientBuilder,
            TaskScheduler taskScheduler,
            PrometheusTelemetryService telemetryService) {
        this.GATEWAY_ID = environment.getRequiredProperty("GATEWAY_ID");
        String IBMS_HOST = environment.getRequiredProperty("IBMS_HOST");
        RestClient restClient = restClientBuilder.baseUrl("http://" + IBMS_HOST).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.gatewayService = factory.createClient(GatewayService.class);
        this.taskScheduler = taskScheduler;
        this.telemetryService = telemetryService;
    }

    @Override
    public void run(String... args) {
        log.info("获取网关配置");
        handle(gatewayService.get(GATEWAY_ID));
        taskScheduler.schedule(this::synchronize, Instant.now().plusSeconds(30));
    }

    private void synchronize() {
        CompletableFuture
                .supplyAsync(() -> {
                    log.info("获取网关配置");
                    return gatewayService.poll(GATEWAY_ID);
                })
                .thenAcceptAsync(this::handle)
                .whenComplete((in, e) -> {
                    Instant nextTime;
                    if (e != null) {
                        log.error("", e);
                        nextTime = Instant.now().plusSeconds(30);
                    } else {
                        nextTime = Instant.now();
                    }
                    taskScheduler.schedule(this::synchronize, nextTime);
                });
    }

    private void handle(ResponseEntity<DeviceGateway> responseEntity) {
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        if (statusCode.isSameCodeAs(HttpStatus.NO_CONTENT)) return;
        if (statusCode.isSameCodeAs(HttpStatus.OK)) {
            DeviceGateway gateway = responseEntity.getBody();
            log.info("网关配置同步成功: {}", gateway);
            telemetryService.reload(gateway);
            taskScheduler.schedule(this::notifySynchronized, Instant.now());
            return;
        }
        log.error("", new ResponseStatusException(statusCode, responseEntity.toString()));
    }

    private void notifySynchronized() {
        log.info("通知 IBMS 已同步...");
        gatewayService.execute(GATEWAY_ID, new SetGatewaySynchronizationStateCommand(SYNCHRONIZED));
    }
}
