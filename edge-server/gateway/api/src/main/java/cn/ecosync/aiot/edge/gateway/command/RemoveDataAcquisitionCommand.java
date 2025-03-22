package cn.ecosync.aiot.edge.gateway.command;

import cn.ecosync.aiot.command.Command;
import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisitionId;
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
