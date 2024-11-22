package cn.ecosync.ibms.command;

import cn.ecosync.ibms.dto.DeviceExtra;
import cn.ecosync.iframework.command.Command;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
