package cn.ecosync.ibms.command;

import cn.ecosync.iframework.command.Command;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class RemoveDeviceCommand implements Command {
    @NotBlank
    private String deviceCode;
}
