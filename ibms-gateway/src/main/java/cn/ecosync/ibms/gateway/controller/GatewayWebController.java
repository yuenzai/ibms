package cn.ecosync.ibms.gateway.controller;

import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectType;
import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.ibms.bacnet.model.BacnetDataPoints;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.gateway.command.PushGatewayConfigurationCommand;
import cn.ecosync.ibms.gateway.command.RemoveDataAcquisitionCommand;
import cn.ecosync.ibms.gateway.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.gateway.event.DeviceDataAcquisitionSavedEvent;
import cn.ecosync.ibms.gateway.model.*;
import cn.ecosync.ibms.gateway.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.gateway.query.SearchDataAcquisitionQuery;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.util.CollectionUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static cn.ecosync.ibms.gateway.model.SynchronizationStateEnum.SYNCHRONIZING;

@Tag(name = "网关API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway")
public class GatewayWebController {
    private static final Logger log = LoggerFactory.getLogger(GatewayWebController.class);

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final Map<DeviceDataAcquisitionId, DataAcquisitionDeferredResult> deferredResultCache = new ConcurrentHashMap<>();

    @Operation(summary = "推送网关配置")
    @PostMapping(headers = "Command-Type=PUSH")
    public void execute() {
        PushGatewayConfigurationCommand command = new PushGatewayConfigurationCommand();
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
    public ResponseEntity<Object> upload(@PathVariable("data-acquisition-code") String dataAcquisitionCode, @RequestPart("file") MultipartFile file) throws IOException {
        DeviceDataPointListener listener = new DeviceDataPointListener();
        EasyExcel.read(file.getInputStream(), listener).sheet().doRead();
        if (!listener.getExceptions().isEmpty()) {
            return new ResponseEntity<>(listener.getExceptions(), HttpStatus.BAD_REQUEST);
        }
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(dataAcquisitionCode);
        SaveDataAcquisitionCommand command = new SaveDataAcquisitionCommand(dataAcquisitionId);
        DeviceDataPoints dataPoints = listener.toDataPoints();
        command.setDataPoints(dataPoints);
        commandBus.execute(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public static class DeviceDataPointListener implements ReadListener<Map<Integer, String>> {
        private final Map<Integer, String> head = new HashMap<>();
        private final List<BacnetDataPoint> bacnetDataPoints = new ArrayList<>();
        private final List<ExcelDataConvertException> exceptions = new ArrayList<>();

        @Override
        public void onException(Exception exception, AnalysisContext context) {
            if (exception instanceof ExcelDataConvertException) {
                exceptions.add((ExcelDataConvertException) exception);
            } else {
                log.error("", exception);
            }
        }

        @Override
        public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
            head.putAll(ConverterUtils.convertToStringMap(headMap, context));
        }

        @Override
        public void invoke(Map<Integer, String> row, AnalysisContext context) {
            bacnetDataPoints.add(toBacnetDataPoint(row));
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        private BacnetDataPoint toBacnetDataPoint(Map<Integer, String> row) {
            String deviceCode = row.get(0);
            String metricName = row.get(1);
            Integer deviceInstance = Integer.parseInt(row.get(2));
            BacnetObjectType objectType = BacnetObjectType.valueOf(row.get(3));
            Integer objectInstance = Integer.parseInt(row.get(4));

            List<DeviceDataPointLabel> labels;
            if (CollectionUtils.notEmpty(head) && head.size() > 5) {
                labels = new ArrayList<>(head.size() - 5);
                for (int i = 5; i < head.size(); i++) {
                    String labelName = head.get(i);
                    String labelValue = row.get(i);
                    labels.add(new DeviceDataPointLabel(labelName, labelValue));
                }
            } else {
                labels = Collections.emptyList();
            }
            return new BacnetDataPoint(
                    new DeviceDataPointId(metricName, deviceCode),
                    deviceInstance,
                    new BacnetObject(objectType, objectInstance),
                    labels
            );
        }

        public DeviceDataPoints toDataPoints() {
            return new BacnetDataPoints(bacnetDataPoints);
        }

        public List<ExcelDataConvertException> getExceptions() {
            return exceptions;
        }
    }
}
