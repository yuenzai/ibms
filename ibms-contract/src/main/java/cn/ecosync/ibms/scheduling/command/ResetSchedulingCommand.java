package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.command.Command;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class ResetSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
}
