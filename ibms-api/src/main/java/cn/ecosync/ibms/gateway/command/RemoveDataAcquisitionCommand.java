package cn.ecosync.ibms.gateway.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
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
