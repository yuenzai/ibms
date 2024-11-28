package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.iframework.command.Command;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RemoveSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
}
