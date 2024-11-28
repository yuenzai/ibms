package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.dto.DeviceExtra;
import cn.ecosync.iframework.command.Command;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateDeviceCommand implements Command {
    @NotBlank
    private String deviceCode;

    private String deviceName;

    private String path;

    private String description;

    @Valid
    @NotNull
    private DeviceExtra deviceExtra;
}
