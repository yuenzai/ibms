package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.TelemetryService;
import cn.ecosync.ibms.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static cn.ecosync.ibms.device.model.SynchronizationStateEnum.SYNCHRONIZED;

public class GatewaySynchronizationService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(GatewaySynchronizationService.class);

    private final Set<String> dataAcquisitionCodes;
    private final DataAcquisitionService dataAcquisitionService;
    private final TaskScheduler taskScheduler;
    private final TelemetryService telemetryService;

    public GatewaySynchronizationService(Environment environment, RestClient.Builder restClientBuilder,
                                         TaskScheduler taskScheduler, TelemetryService telemetryService) {
        Set<String> dataAcquisitionCodes = StringUtils.commaDelimitedListToSet(environment.getProperty("DATA_ACQUISITION_CODES"));
        Assert.notEmpty(dataAcquisitionCodes, "Environment variable required: DATA_ACQUISITION_CODES");
        this.dataAcquisitionCodes = dataAcquisitionCodes;
        String ibmsHost = environment.getProperty("IBMS_HOST");
        Assert.hasText(ibmsHost, "Environment variable required: IBMS_HOST");
        RestClient restClient = restClientBuilder.baseUrl("http://" + ibmsHost).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.dataAcquisitionService = factory.createClient(DataAcquisitionService.class);
        this.taskScheduler = taskScheduler;
        this.telemetryService = telemetryService;
    }

    @Override
    public void run(String... args) {
        log.info("获取网关配置");
        for (String dataAcquisitionCode : dataAcquisitionCodes) {
            if (!StringUtils.hasText(dataAcquisitionCode)) continue;
            DeviceDataAcquisitionSynchronizationService service = newInstance(dataAcquisitionCode);
            service.handle(dataAcquisitionService.get(dataAcquisitionCode));
            taskScheduler.schedule(service, Instant.now().plusSeconds(30));
        }
    }

    private static class DeviceDataAcquisitionSynchronizationService implements Runnable {
        private final String dataAcquisitionCode;
        private final DataAcquisitionService dataAcquisitionService;
        private final TaskScheduler taskScheduler;
        private final TelemetryService telemetryService;

        public DeviceDataAcquisitionSynchronizationService(
                String dataAcquisitionCode,
                DataAcquisitionService dataAcquisitionService,
                TaskScheduler taskScheduler,
                TelemetryService telemetryService) {
            Assert.hasText(dataAcquisitionCode, "dataAcquisitionCode must not be null");
            this.dataAcquisitionCode = dataAcquisitionCode;
            this.dataAcquisitionService = dataAcquisitionService;
            this.taskScheduler = taskScheduler;
            this.telemetryService = telemetryService;
        }

        @Override
        public void run() {
            CompletableFuture
                    .supplyAsync(() -> {
                        log.info("获取网关配置");
                        return dataAcquisitionService.poll(dataAcquisitionCode);
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
                        taskScheduler.schedule(this, nextTime);
                    });
        }

        private void handle(ResponseEntity<DeviceDataAcquisition> responseEntity) {
            HttpStatusCode statusCode = responseEntity.getStatusCode();
            if (statusCode.isSameCodeAs(HttpStatus.NO_CONTENT)) {
//                if (telemetryService.remove(new DeviceDataAcquisitionId(dataAcquisitionCode))) {
//                    log.info("采集配置不存在[{}]", dataAcquisitionCode);
//                    telemetryService.reload();
//                }
                telemetryService.remove(new DeviceDataAcquisitionId(dataAcquisitionCode));
                telemetryService.reload();
                return;
            }
            if (statusCode.isSameCodeAs(HttpStatus.ACCEPTED)) return;
            if (statusCode.isSameCodeAs(HttpStatus.OK)) {
                DeviceDataAcquisition dataAcquisition = responseEntity.getBody();
                Assert.notNull(dataAcquisition, "DataAcquisition must not be null when http status code is 200");
                log.info("采集配置同步成功[{}]", dataAcquisition);
                telemetryService.add(dataAcquisition);
                telemetryService.reload();
                taskScheduler.schedule(this::notifySynchronized, Instant.now());
                return;
            }
            log.error("", new ResponseStatusException(statusCode, responseEntity.toString()));
        }

        private void notifySynchronized() {
            log.info("通知 IBMS 已同步...");
            DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(dataAcquisitionCode);
            SaveDataAcquisitionCommand command = new SaveDataAcquisitionCommand(dataAcquisitionId);
            command.setSynchronizationState(SYNCHRONIZED);
            dataAcquisitionService.execute(dataAcquisitionCode, command);
        }
    }

    private DeviceDataAcquisitionSynchronizationService newInstance(String dataAcquisitionCode) {
        return new DeviceDataAcquisitionSynchronizationService(dataAcquisitionCode, dataAcquisitionService, taskScheduler, telemetryService);
    }
}
