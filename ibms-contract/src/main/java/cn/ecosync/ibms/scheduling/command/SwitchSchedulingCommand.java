package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.iframework.command.Command;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class SwitchSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
    @NotNull
    private Boolean enabled;
}
