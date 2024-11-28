package cn.ecosync.ibms.command;

import cn.ecosync.iframework.command.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SwitchSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
    @NotNull
    private Boolean enabled;
}
