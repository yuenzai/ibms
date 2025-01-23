package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectType;
import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.ibms.device.command.AddDataAcquisitionCommand;
import cn.ecosync.ibms.device.command.RemoveDataAcquisitionCommand;
import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataPointId;
import cn.ecosync.ibms.device.model.DeviceDataPointLabel;
import cn.ecosync.ibms.device.model.IDeviceDataAcquisition;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.device.query.SearchDataAcquisitionQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.util.CollectionUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Tag(name = "设备数据采集")
@RestController
@RequiredArgsConstructor
@RequestMapping("/device-daq")
public class DeviceDataAcquisitionWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @Operation(summary = "新增数据采集")
    @PostMapping("/add")
    public void execute(@RequestBody @Validated AddDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "修改数据采集")
    @PostMapping("/save")
    public void execute(@RequestBody @Validated SaveDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "删除数据采集")
    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @Operation(summary = "获取数据采集")
    @PostMapping("/get")
    public IDeviceDataAcquisition get(@RequestBody @Validated GetDataAcquisitionQuery query) {
        return queryBus.execute(query);
    }

    @Operation(summary = "查询数据采集")
    @PostMapping("/search")
    public PagedModel<DeviceDataAcquisition> search(@RequestBody @Validated SearchDataAcquisitionQuery query) {
        return new PagedModel<>(queryBus.execute(query));
    }

    @Operation(summary = "导入 BACnet 点位")
    @PostMapping("/{data-acquisition-code}/bacnet-data-point/import")
    public ResponseEntity<Object> upload(@PathVariable("data-acquisition-code") String dataAcquisitionCode, @RequestPart("file") MultipartFile file) throws IOException {
        DeviceDataPointListener listener = new DeviceDataPointListener();
        EasyExcel.read(file.getInputStream(), listener).sheet().doRead();
        if (!listener.getExceptions().isEmpty()) {
            return new ResponseEntity<>(listener.getExceptions(), HttpStatus.BAD_REQUEST);
        }
        List<BacnetDataPoint> bacnetDataPoints = listener.getBacnetDataPoints();
        SaveDataAcquisitionCommand command = new SaveDataAcquisitionCommand.Bacnet(dataAcquisitionCode, null, bacnetDataPoints);
        commandBus.execute(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Slf4j
    @Getter
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
    }
}
