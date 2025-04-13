package cn.ecosync.aiot.edge.gateway.controller;

import cn.ecosync.aiot.command.CommandBus;
import cn.ecosync.aiot.edge.gateway.command.*;
import cn.ecosync.aiot.edge.gateway.exception.ExcelAnalysisException;
import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisition;
import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.aiot.edge.gateway.query.GetDataAcquisitionQuery;
import cn.ecosync.aiot.edge.gateway.query.SearchDataAcquisitionQuery;
import cn.ecosync.aiot.query.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 数据采集 restful API
 *
 * @see DeviceDataAcquisition 数据采集
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/data-acquisition")
public class DataAcquisitionWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;
//    private final Map<DeviceDataAcquisitionId, DataAcquisitionDeferredResult> deferredResultCache = new ConcurrentHashMap<>();

    /**
     * 重载遥测服务
     */
    @PostMapping(path = "/reload")
    public void execute() {
        ReloadTelemetryServiceCommand command = new ReloadTelemetryServiceCommand();
        commandBus.execute(command);
    }

    /**
     * 保存数据采集
     *
     * @param command 保存数据采集命令
     */
    @PostMapping(path = "/{data-acquisition-code}/save")
    public void execute(@RequestBody @Validated SaveDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    /**
     * 删除数据采集
     *
     * @param command 删除数据采集命令
     */
    @PostMapping(path = "/{data-acquisition-code}/remove")
    public void execute(@RequestBody @Validated RemoveDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    /**
     * 获取数据采集
     *
     * @param dataAcquisitionCode 数据采集编码
     * @return 数据采集
     */
    @GetMapping(path = "/{data-acquisition-code}")
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

//    @Operation(hidden = true)
//    @GetMapping(path = "/{data-acquisition-code}", headers = "Query-Type=POLL")
//    public DeferredResult<ResponseEntity<DeviceDataAcquisition>> poll(@PathVariable("data-acquisition-code") String dataAcquisitionCode) {
//        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(dataAcquisitionCode);
//        GetDataAcquisitionQuery query = new GetDataAcquisitionQuery(dataAcquisitionId);
//        DeviceDataAcquisition dataAcquisition = queryBus.execute(query);
//        DataAcquisitionDeferredResult deferredResult = new DataAcquisitionDeferredResult(dataAcquisition);
//        if (dataAcquisition != null && dataAcquisition.getSynchronizationState() == SYNCHRONIZING) {
//            deferredResult.setResult(new ResponseEntity<>(dataAcquisition, HttpStatus.OK));
//        } else {
//            deferredResultCache.put(dataAcquisitionId, deferredResult);
//        }
//        return deferredResult;
//    }
//
//    public static class DataAcquisitionDeferredResult extends DeferredResult<ResponseEntity<DeviceDataAcquisition>> {
//        private final DeviceDataAcquisition dataAcquisition;
//
//        public DataAcquisitionDeferredResult(DeviceDataAcquisition dataAcquisition) {
//            super(null, () -> toTimeoutResult(dataAcquisition));
//            this.dataAcquisition = dataAcquisition;
//        }
//
//        public void setTimeoutResult() {
//            setResult(toTimeoutResult(dataAcquisition));
//        }
//
//        private static ResponseEntity<DeviceDataAcquisition> toTimeoutResult(DeviceDataAcquisition dataAcquisition) {
//            return new ResponseEntity<>(dataAcquisition != null ? HttpStatus.ACCEPTED : HttpStatus.NO_CONTENT);
//        }
//    }

//    @EventListener(DeviceDataAcquisitionSavedEvent.class)
//    public void onListen(DeviceDataAcquisitionSavedEvent event) {
//        DeviceDataAcquisition dataAcquisition = event.getDataAcquisition();
//        if (dataAcquisition.getSynchronizationState() == SYNCHRONIZING) {
//            DeviceDataAcquisitionId dataAcquisitionId = dataAcquisition.getDataAcquisitionId();
//            DataAcquisitionDeferredResult deferredResult = deferredResultCache.remove(dataAcquisitionId);
//            if (deferredResult != null) {
//                log.info("中断轮询[key={}]", dataAcquisitionId);
//                deferredResult.setTimeoutResult();
//            }
//        }
//    }

    /**
     * 查询数据采集，参数{@code page}和{@code pageSize}是可选的，不提供参数会返回所有数据，如果提供则两个参数都需要传递
     *
     * @param page     分页页码，从零开始（可选）
     * @param pageSize 每页返回的数据大小（可选）
     * @return 数据采集，不管有没有传递{@code page}和{@code pageSize}，返回的数据结构都是分页结构
     */
    @GetMapping
    public PagedModel<DeviceDataAcquisition> search(@RequestParam(name = "page", required = false) Integer page,
                                                    @RequestParam(name = "pagesize", required = false) Integer pageSize) {
        SearchDataAcquisitionQuery query = new SearchDataAcquisitionQuery(page, pageSize);
        Page<DeviceDataAcquisition> dataAcquisitions = queryBus.execute(query);
        return new PagedModel<>(dataAcquisitions);
    }

    /**
     * 导入 BACnet 点位
     *
     * @param dataAcquisitionCode 数据采集编码
     * @param file                Excel格式的文件
     * @return HTTP 状态码，正常返回 {@link org.springframework.http.HttpStatus#OK 200}，
     * 数据格式解析错误返回 {@link org.springframework.http.HttpStatus#BAD_REQUEST 400}，并且响应体包含错误的单元格信息
     * @throws IOException
     */
    @PostMapping(path = "/{data-acquisition-code}/import-bacnet-point")
    public ResponseEntity<Object> bacnetImport(@PathVariable("data-acquisition-code") String dataAcquisitionCode, @RequestPart("file") MultipartFile file) throws IOException {
        ImportBacnetDataPointsCommand command = new ImportBacnetDataPointsCommand(dataAcquisitionCode, file.getInputStream());
        try {
            commandBus.execute(command);
        } catch (ExcelAnalysisException e) {
            return new ResponseEntity<>(e.getCells(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 导入设备信息
     *
     * @param dataAcquisitionCode 数据采集编码
     * @param file                Excel格式的文件
     * @return HTTP 状态码，正常返回 {@link org.springframework.http.HttpStatus#OK 200}，
     * 数据格式解析错误返回 {@link org.springframework.http.HttpStatus#BAD_REQUEST 400}，并且响应体包含错误的单元格信息
     * @throws IOException
     */
    @PostMapping(path = "/{data-acquisition-code}/import-device-info")
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
