package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceId;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;

@Getter
@ToString
public class RemoveDeviceCommand implements Command {
    @Valid
    @JsonUnwrapped
    private DeviceId deviceId;
}
