package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.iframework.command.Command;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public abstract class AddDeviceCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId daqId;

    protected AddDeviceCommand() {
    }

    public abstract List<DeviceModel> newDevices();
}
