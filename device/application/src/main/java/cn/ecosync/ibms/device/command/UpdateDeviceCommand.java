package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceProperties;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class UpdateDeviceCommand implements Command {
    @NotBlank
    private String deviceCode;
    private String deviceName;
    private String path;
    private String description;
    @Valid
    private DeviceProperties properties;

    public DeviceId toDeviceId() {
        return new DeviceId(deviceCode);
    }
}
