package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionProperties;
import cn.ecosync.iframework.command.Command;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AddDeviceDataAcquisitionCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId daqId;
    @Valid
    @NotNull
    private DeviceDataAcquisitionProperties daqProperties;
}
