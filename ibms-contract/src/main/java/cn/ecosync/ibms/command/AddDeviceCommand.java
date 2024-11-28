package cn.ecosync.ibms.command;

import cn.ecosync.ibms.dto.DeviceExtra;
import cn.ecosync.iframework.command.Command;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AddDeviceCommand implements Command {
    @NotBlank
    private String deviceCode;

    private String deviceName;

    private String path;

    private String description;

    @Valid
    @NotNull
    private DeviceExtra deviceExtra;
}
