//package cn.ecosync.ibms.gateway.service;
//
//import cn.ecosync.ibms.gateway.command.SaveDataAcquisitionCommand;
//import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
//import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
//import cn.ecosync.ibms.util.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.util.Assert;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.Instant;
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//
//import static cn.ecosync.ibms.gateway.model.SynchronizationStateEnum.SYNCHRONIZED;
//
//public class GatewaySynchronizationService implements ApplicationRunner {
//    private static final Logger log = LoggerFactory.getLogger(GatewaySynchronizationService.class);
//
//    private final DataAcquisitionHttpService dataAcquisitionService;
//    private final TelemetryService telemetryService;
//    private final TaskScheduler taskScheduler;
//    private final Set<String> dataAcquisitionCodes;
//
//    public GatewaySynchronizationService(DataAcquisitionHttpService dataAcquisitionService, TelemetryService telemetryService,
//                                         TaskScheduler taskScheduler, Environment environment) {
//        this.dataAcquisitionService = dataAcquisitionService;
//        this.telemetryService = telemetryService;
//        this.taskScheduler = taskScheduler;
//        Set<String> dataAcquisitionCodes = StringUtils.commaDelimitedListToSet(environment.getProperty("DATA_ACQUISITION_CODES"));
//        Assert.notEmpty(dataAcquisitionCodes, "Environment variable required: DATA_ACQUISITION_CODES");
//        this.dataAcquisitionCodes = dataAcquisitionCodes;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//        log.info("获取网关配置");
//        for (String dataAcquisitionCode : dataAcquisitionCodes) {
//            if (!StringUtils.hasText(dataAcquisitionCode)) continue;
//            DeviceDataAcquisitionSynchronizationService service = newInstance(dataAcquisitionCode);
//            service.handle(dataAcquisitionService.get(dataAcquisitionCode));
//            taskScheduler.schedule(service, Instant.now().plusSeconds(30));
//        }
//    }
//
//    private static class DeviceDataAcquisitionSynchronizationService implements Runnable {
//        private final String dataAcquisitionCode;
//        private final DataAcquisitionHttpService dataAcquisitionService;
//        private final TaskScheduler taskScheduler;
//        private final TelemetryService telemetryService;
//
//        public DeviceDataAcquisitionSynchronizationService(
//                String dataAcquisitionCode,
//                DataAcquisitionHttpService dataAcquisitionService,
//                TaskScheduler taskScheduler,
//                TelemetryService telemetryService) {
//            Assert.hasText(dataAcquisitionCode, "dataAcquisitionCode must not be null");
//            this.dataAcquisitionCode = dataAcquisitionCode;
//            this.dataAcquisitionService = dataAcquisitionService;
//            this.taskScheduler = taskScheduler;
//            this.telemetryService = telemetryService;
//        }
//
//        @Override
//        public void run() {
//            CompletableFuture
//                    .supplyAsync(() -> {
//                        log.info("获取网关配置");
//                        return dataAcquisitionService.poll(dataAcquisitionCode);
//                    })
//                    .thenAcceptAsync(this::handle)
//                    .whenComplete((in, e) -> {
//                        Instant nextTime;
//                        if (e != null) {
//                            log.error("", e);
//                            nextTime = Instant.now().plusSeconds(30);
//                        } else {
//                            nextTime = Instant.now();
//                        }
//                        taskScheduler.schedule(this, nextTime);
//                    });
//        }
//
//        private void handle(ResponseEntity<DeviceDataAcquisition> responseEntity) {
//            HttpStatusCode statusCode = responseEntity.getStatusCode();
//            if (statusCode.isSameCodeAs(HttpStatus.NO_CONTENT)) {
//                telemetryService.remove(new DeviceDataAcquisitionId(dataAcquisitionCode));
//                telemetryService.reload();
//                return;
//            }
//            if (statusCode.isSameCodeAs(HttpStatus.ACCEPTED)) return;
//            if (statusCode.isSameCodeAs(HttpStatus.OK)) {
//                DeviceDataAcquisition dataAcquisition = responseEntity.getBody();
//                Assert.notNull(dataAcquisition, "DataAcquisition must not be null when http status code is 200");
//                log.info("采集配置同步成功[{}]", dataAcquisition);
//                telemetryService.add(dataAcquisition);
//                telemetryService.reload();
//                taskScheduler.schedule(this::notifySynchronized, Instant.now());
//                return;
//            }
//            log.error("", new ResponseStatusException(statusCode, responseEntity.toString()));
//        }
//
//        private void notifySynchronized() {
//            log.info("通知 IBMS 已同步...");
//            DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(dataAcquisitionCode);
//            SaveDataAcquisitionCommand command = new SaveDataAcquisitionCommand(dataAcquisitionId);
//            command.setSynchronizationState(SYNCHRONIZED);
//            dataAcquisitionService.execute(dataAcquisitionCode, command);
//        }
//    }
//
//    private DeviceDataAcquisitionSynchronizationService newInstance(String dataAcquisitionCode) {
//        return new DeviceDataAcquisitionSynchronizationService(dataAcquisitionCode, dataAcquisitionService, taskScheduler, telemetryService);
//    }
//}
