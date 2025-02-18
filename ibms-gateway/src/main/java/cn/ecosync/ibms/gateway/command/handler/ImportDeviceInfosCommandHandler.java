package cn.ecosync.ibms.gateway.command.handler;

import cn.ecosync.ibms.bacnet.command.ImportDeviceInfosCommand;
import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.gateway.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.gateway.exception.ExcelAnalysisException;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.model.DeviceDataPointLabel;
import cn.ecosync.ibms.gateway.model.DeviceInfos;
import cn.ecosync.ibms.gateway.model.DeviceInfos.DeviceInfo;
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
public class ImportDeviceInfosCommandHandler implements CommandHandler<ImportDeviceInfosCommand> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;
    private final EventBus eventBus;

    public ImportDeviceInfosCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        this.dataAcquisitionRepository = dataAcquisitionRepository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public void handle(ImportDeviceInfosCommand command) {
        DeviceInfoListener listener = new DeviceInfoListener();
        EasyExcel.read(command.getInputStream(), listener).sheet().doRead();
        if (!listener.getExceptions().isEmpty()) {
            List<ExcelAnalysisException.Cell> cells = listener.getExceptions().stream()
                    .map(in -> new ExcelAnalysisException.Cell(in.getRowIndex(), in.getColumnIndex(), in.getCellData()))
                    .collect(Collectors.toList());
            throw new ExcelAnalysisException(cells);
        }
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(command.getDataAcquisitionCode());
        SaveDataAcquisitionCommand command2 = new SaveDataAcquisitionCommand(dataAcquisitionId);
        DeviceInfos deviceInfos = listener.toDeviceInfos();
        command2.setDeviceInfos(deviceInfos);
        dataAcquisitionRepository.save(command2)
                .forEach(eventBus::publish);
    }

    public static class DeviceInfoListener implements ReadListener<Map<Integer, String>> {
        private final Map<Integer, String> head = new HashMap<>();
        private final List<DeviceInfo> deviceInfos = new ArrayList<>();
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
            deviceInfos.add(toDeviceInfo(row));
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        private DeviceInfo toDeviceInfo(Map<Integer, String> row) {
            String deviceCode = row.get(0);
            List<DeviceDataPointLabel> labels;
            if (CollectionUtils.notEmpty(head) && head.size() > 1) {
                labels = new ArrayList<>(head.size() - 1);
                for (int i = 1; i < head.size(); i++) {
                    String labelName = head.get(i);
                    String labelValue = row.get(i);
                    labels.add(new DeviceDataPointLabel(labelName, labelValue));
                }
            } else {
                labels = Collections.emptyList();
            }
            return new DeviceInfo(deviceCode, labels);
        }

        public DeviceInfos toDeviceInfos() {
            return new DeviceInfos(deviceInfos);
        }

        public List<ExcelDataConvertException> getExceptions() {
            return exceptions;
        }
    }
}
