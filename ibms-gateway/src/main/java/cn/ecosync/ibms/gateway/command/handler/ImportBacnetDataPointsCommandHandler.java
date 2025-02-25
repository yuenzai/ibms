package cn.ecosync.ibms.gateway.command.handler;

import cn.ecosync.ibms.bacnet.command.ImportBacnetDataPointsCommand;
import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.gateway.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.gateway.exception.ExcelAnalysisException;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionType;
import cn.ecosync.ibms.gateway.model.LabelTable;
import cn.ecosync.ibms.util.SimpleReadListener;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        SimpleReadListener listener = new SimpleReadListener();
        EasyExcel.read(command.getInputStream(), listener).sheet().doRead();
        if (!listener.getExceptions().isEmpty()) {
            List<ExcelAnalysisException.Cell> cells = listener.getExceptions().stream()
                    .map(in -> new ExcelAnalysisException.Cell(in.getRowIndex(), in.getColumnIndex(), in.getCellData()))
                    .collect(Collectors.toList());
            throw new ExcelAnalysisException(cells);
        }
        DeviceDataAcquisitionId dataAcquisitionId = new DeviceDataAcquisitionId(command.getDataAcquisitionCode());
        SaveDataAcquisitionCommand command2 = new SaveDataAcquisitionCommand(dataAcquisitionId);
        LabelTable labelTable = new LabelTable(listener.getHead(), listener.getBody());
        command2.withDataPoints(DeviceDataAcquisitionType.BACNET, labelTable);
        dataAcquisitionRepository.save(command2)
                .forEach(eventBus::publish);
    }
}
