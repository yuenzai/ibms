package cn.ecosync.aiot.edge.gateway.command.handler;

import cn.ecosync.aiot.command.CommandHandler;
import cn.ecosync.aiot.edge.gateway.command.SaveDataAcquisitionCommand;
import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisitionRepository;
import org.springframework.transaction.annotation.Transactional;

public class SaveDataAcquisitionCommandHandler implements CommandHandler<SaveDataAcquisitionCommand> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;

    public SaveDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        this.dataAcquisitionRepository = dataAcquisitionRepository;
    }

    @Override
    @Transactional
    public void handle(SaveDataAcquisitionCommand command) {
        dataAcquisitionRepository.save(command);
    }
}
