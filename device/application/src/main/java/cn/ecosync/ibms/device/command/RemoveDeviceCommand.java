package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceId;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class RemoveDeviceCommand implements Command {
    @NotBlank
    private String deviceCode;

    public DeviceId toDeviceId() {
        return new DeviceId(deviceCode);
    }
}
