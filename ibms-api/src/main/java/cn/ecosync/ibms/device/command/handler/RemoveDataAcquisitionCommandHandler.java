package cn.ecosync.ibms.device.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.device.command.RemoveDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RemoveDataAcquisitionCommandHandler implements CommandHandler<RemoveDataAcquisitionCommand> {
    private final DeviceDataAcquisitionRepository dataAcquisitionRepository;

    @Override
    @Transactional
    public void handle(RemoveDataAcquisitionCommand command) {
        DeviceDataAcquisitionId dataAcquisitionId = command.getDataAcquisitionId();
        dataAcquisitionRepository.remove(dataAcquisitionId);
    }
}
