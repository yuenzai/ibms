package cn.ecosync.ibms.command;

import cn.ecosync.iframework.command.Command;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class RemoveSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
}
