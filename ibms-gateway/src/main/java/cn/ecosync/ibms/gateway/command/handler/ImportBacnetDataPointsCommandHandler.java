package cn.ecosync.ibms.gateway.command.handler;

import cn.ecosync.ibms.bacnet.command.ImportBacnetDataPointsCommand;
import cn.ecosync.ibms.bacnet.model.BacnetDataPoints;
import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.gateway.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.gateway.exception.ExcelAnalysisException;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.ParsingUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        BacnetDataPoints dataPoints = listener.toDataPoints();
        command2.setDataPoints(dataPoints);
        dataAcquisitionRepository.save(command2)
                .forEach(eventBus::publish);
    }

    public static class DeviceDataPointListener implements ReadListener<Map<Integer, String>> {
        private final Map<Integer, String> head = new HashMap<>();
        private final List<Map<Integer, String>> body = new ArrayList<>();
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
            Map<Integer, String> head = ConverterUtils.convertToStringMap(headMap, context);
            for (Map.Entry<Integer, String> entry : head.entrySet()) {
                this.head.put(entry.getKey(), String.join("_", ParsingUtils.splitCamelCaseToLower(entry.getValue())));
            }
        }

        @Override
        public void invoke(Map<Integer, String> row, AnalysisContext context) {
            body.add(row);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        public BacnetDataPoints toDataPoints() {
            return new BacnetDataPoints(head, body);
        }

        public List<ExcelDataConvertException> getExceptions() {
            return exceptions;
        }
    }
}
