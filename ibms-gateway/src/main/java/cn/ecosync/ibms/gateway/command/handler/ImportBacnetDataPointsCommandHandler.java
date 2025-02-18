package cn.ecosync.ibms.gateway.command.handler;

import cn.ecosync.ibms.bacnet.command.ImportBacnetDataPointsCommand;
import cn.ecosync.ibms.bacnet.dto.BacnetObject;
import cn.ecosync.ibms.bacnet.dto.BacnetObjectType;
import cn.ecosync.ibms.bacnet.model.BacnetDataPoint;
import cn.ecosync.ibms.bacnet.model.BacnetDataPoints;
import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.gateway.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.gateway.exception.ExcelAnalysisException;
import cn.ecosync.ibms.gateway.model.*;
import cn.ecosync.ibms.util.CollectionUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ImportBacnetDataPointsCommandHandler implements CommandHandler<ImportBacnetDataPointsCommand> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private final EventBus eventBus;

    public ImportBacnetDataPointsCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        this.dataAcquisitionRepository = dataAcquisitionRepository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public void handle(ImportBacnetDataPointsCommand command) {
        DeviceDataPointListener listener = new DeviceDataPointListener();
        EasyExcel.read(command.getInputStream(), listener).sheet().doRead();
        if (!listener.getExceptions().isEmpty()) {
            List<ExcelAnalysisException.Cell> cells = listener.getExceptions().stream()
                    .map(in -> new ExcelAnalysisException.Cell(in.getRowIndex(), in.getColumnIndex(), in.getCellData()))
                    .collect(Collectors.toList());
            throw new ExcelAnalysisException(cells);
        }
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(command.getDataAcquisitionCode());
        SaveDataAcquisitionCommand command2 = new SaveDataAcquisitionCommand(dataAcquisitionId);
        DeviceDataPoints dataPoints = listener.toDataPoints();
        command2.setDataPoints(dataPoints);
        dataAcquisitionRepository.save(command2)
                .forEach(eventBus::publish);
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
