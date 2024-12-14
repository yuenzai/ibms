package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceExtra;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.iframework.command.Command;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AddDeviceCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceId deviceId;
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId daqId;
    private String deviceName;
    private String path;
    private String description;
    @Valid
    @NotNull
    private DeviceExtra deviceExtra;
}
