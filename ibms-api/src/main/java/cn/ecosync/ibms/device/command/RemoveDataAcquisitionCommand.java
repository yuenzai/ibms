package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.ToString;

@ToString
public class RemoveDataAcquisitionCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;

    public DeviceDataAcquisitionId getDataAcquisitionId() {
        return dataAcquisitionId;
    }
}
