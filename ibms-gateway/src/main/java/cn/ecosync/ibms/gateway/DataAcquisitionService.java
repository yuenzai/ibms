package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import org.springframework.http.ResponseEntity;

public interface DataAcquisitionService {
    ResponseEntity<DeviceDataAcquisition> get(String dataAcquisitionCode);

    ResponseEntity<DeviceDataAcquisition> poll(String dataAcquisitionCode);

    void execute(String dataAcquisitionCode, SaveDataAcquisitionCommand command);
}
