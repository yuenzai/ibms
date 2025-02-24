package cn.ecosync.ibms.gateway.controller;

import cn.ecosync.ibms.bacnet.command.ImportBacnetDataPointsCommand;
import cn.ecosync.ibms.bacnet.command.ImportDeviceInfosCommand;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.gateway.command.ReloadTelemetryServiceCommand;
import cn.ecosync.ibms.gateway.command.RemoveDataAcquisitionCommand;
import cn.ecosync.ibms.gateway.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.gateway.event.DeviceDataAcquisitionSavedEvent;
import cn.ecosync.ibms.gateway.exception.ExcelAnalysisException;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.gateway.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.gateway.query.SearchDataAcquisitionQuery;
import cn.ecosync.ibms.query.QueryBus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.ecosync.ibms.gateway.model.SynchronizationStateEnum.SYNCHRONIZING;

@Tag(name = "网关API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway")
public class GatewayWebController implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(GatewayWebController.class);

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final Map<DeviceDataAcquisitionId, DataAcquisitionDeferredResult> deferredResultCache = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) {
        ReloadTelemetryServiceCommand command = new ReloadTelemetryServiceCommand();
        commandBus.execute(command);
    }

    @Operation(summary = "重载遥测服务")
    @PostMapping(headers = "Command-Type=RELOAD")
    public void execute() {
        ReloadTelemetryServiceCommand command = new ReloadTelemetryServiceCommand();
        commandBus.execute(command);
    }

    @Operation(summary = "保存数据采集")
    @PostMapping(path = "/data-acquisition/{data-acquisition-code}", headers = "Command-Type=SAVE")
    public void execute(@RequestBody @Validated SaveDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "删除数据采集")
    @PostMapping(path = "/data-acquisition/{data-acquisition-code}", headers = "Command-Type=REMOVE")
    public void execute(@RequestBody @Validated RemoveDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "获取数据采集")
    @GetMapping(path = "/data-acquisition/{data-acquisition-code}", headers = "Query-Type=GET")
    public ResponseEntity<DeviceDataAcquisition> get(@PathVariable("data-acquisition-code") String dataAcquisitionCode) {
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(dataAcquisitionCode);
        GetDataAcquisitionQuery query = new GetDataAcquisitionQuery(dataAcquisitionId);
        DeviceDataAcquisition dataAcquisition = queryBus.execute(query);
        if (dataAcquisition != null) {
            return new ResponseEntity<>(dataAcquisition, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Operation(hidden = true)
    @GetMapping(path = "/data-acquisition/{data-acquisition-code}", headers = "Query-Type=POLL")
    public DeferredResult<ResponseEntity<DeviceDataAcquisition>> poll(@PathVariable("data-acquisition-code") String dataAcquisitionCode) {
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(dataAcquisitionCode);
        GetDataAcquisitionQuery query = new GetDataAcquisitionQuery(dataAcquisitionId);
        DeviceDataAcquisition dataAcquisition = queryBus.execute(query);
        DataAcquisitionDeferredResult deferredResult = new DataAcquisitionDeferredResult(dataAcquisition);
        if (dataAcquisition != null && dataAcquisition.getSynchronizationState() == SYNCHRONIZING) {
            deferredResult.setResult(new ResponseEntity<>(dataAcquisition, HttpStatus.OK));
        } else {
            deferredResultCache.put(dataAcquisitionId, deferredResult);
        }
        return deferredResult;
    }

    public static class DataAcquisitionDeferredResult extends DeferredResult<ResponseEntity<DeviceDataAcquisition>> {
        private final DeviceDataAcquisition dataAcquisition;

        public DataAcquisitionDeferredResult(DeviceDataAcquisition dataAcquisition) {
            super(null, () -> toTimeoutResult(dataAcquisition));
            this.dataAcquisition = dataAcquisition;
        }

        public void setTimeoutResult() {
            setResult(toTimeoutResult(dataAcquisition));
        }

        private static ResponseEntity<DeviceDataAcquisition> toTimeoutResult(DeviceDataAcquisition dataAcquisition) {
            return new ResponseEntity<>(dataAcquisition != null ? HttpStatus.ACCEPTED : HttpStatus.NO_CONTENT);
        }
    }

    @EventListener(DeviceDataAcquisitionSavedEvent.class)
    public void onListen(DeviceDataAcquisitionSavedEvent event) {
        DeviceDataAcquisition dataAcquisition = event.getDataAcquisition();
        if (dataAcquisition.getSynchronizationState() == SYNCHRONIZING) {
            DeviceDataAcquisitionId dataAcquisitionId = dataAcquisition.getDataAcquisitionId();
            DataAcquisitionDeferredResult deferredResult = deferredResultCache.remove(dataAcquisitionId);
            if (deferredResult != null) {
                log.info("中断轮询[key={}]", dataAcquisitionId);
                deferredResult.setTimeoutResult();
            }
        }
    }

    @Operation(summary = "查询数据采集")
    @GetMapping(path = "/data-acquisition", headers = "Query-Type=SEARCH")
    public PagedModel<DeviceDataAcquisition> search(@RequestParam(name = "page", required = false) Integer page,
                                                    @RequestParam(name = "pagesize", required = false) Integer pageSize) {
        SearchDataAcquisitionQuery query = new SearchDataAcquisitionQuery(page, pageSize);
        Page<DeviceDataAcquisition> dataAcquisitions = queryBus.execute(query);
        return new PagedModel<>(dataAcquisitions);
    }

    @Operation(summary = "导入 BACnet 点位")
    @PostMapping(path = "/data-acquisition/{data-acquisition-code}", headers = "Command-Type=BACNET_IMPORT")
    public ResponseEntity<Object> bacnetImport(@PathVariable("data-acquisition-code") String dataAcquisitionCode, @RequestPart("file") MultipartFile file) throws IOException {
        ImportBacnetDataPointsCommand command = new ImportBacnetDataPointsCommand(dataAcquisitionCode, file.getInputStream());
        try {
            commandBus.execute(command);
        } catch (ExcelAnalysisException e) {
            return new ResponseEntity<>(e.getCells(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "导入设备信息")
    @PostMapping(path = "/data-acquisition/{data-acquisition-code}", headers = "Command-Type=DEVICE_INFO_IMPORT")
    public ResponseEntity<Object> deviceInfoImport(@PathVariable("data-acquisition-code") String dataAcquisitionCode, @RequestPart("file") MultipartFile file) throws IOException {
        ImportDeviceInfosCommand command = new ImportDeviceInfosCommand(dataAcquisitionCode, file.getInputStream());
        try {
            commandBus.execute(command);
        } catch (ExcelAnalysisException e) {
            return new ResponseEntity<>(e.getCells(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
