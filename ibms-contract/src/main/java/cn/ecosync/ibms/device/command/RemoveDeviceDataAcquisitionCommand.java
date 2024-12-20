package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.iframework.command.Command;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RemoveDeviceDataAcquisitionCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId daqId;
}
