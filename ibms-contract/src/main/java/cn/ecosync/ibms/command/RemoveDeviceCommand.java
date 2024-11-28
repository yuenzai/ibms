package cn.ecosync.ibms.command;

import cn.ecosync.iframework.command.Command;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RemoveDeviceCommand implements Command {
    @NotBlank
    private String deviceCode;
}
