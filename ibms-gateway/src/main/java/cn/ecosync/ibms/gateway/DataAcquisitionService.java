package cn.ecosync.ibms.gateway;

import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/ibms/device-data-acquisition")
public interface DataAcquisitionService {
    @HttpExchange(method = "GET", url = "/{data-acquisition-code}", headers = "Query-Type=GET")
    ResponseEntity<DeviceDataAcquisition> get(@PathVariable("data-acquisition-code") String dataAcquisitionCode);

    @HttpExchange(method = "GET", url = "/{data-acquisition-code}", headers = "Query-Type=POLL")
    ResponseEntity<DeviceDataAcquisition> poll(@PathVariable("data-acquisition-code") String dataAcquisitionCode);

    @HttpExchange(method = "POST", url = "/{data-acquisition-code}", headers = "Command-Type=SAVE")
    void execute(@PathVariable("data-acquisition-code") String dataAcquisitionCode, @RequestBody SaveDataAcquisitionCommand command);
}
