package cn.ecosync.ibms.command;

import cn.ecosync.ibms.dto.SchedulingTaskParams;
import cn.ecosync.ibms.dto.SchedulingTrigger;
import cn.ecosync.iframework.command.Command;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AddSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
    @Valid
    @NotNull
    private SchedulingTrigger schedulingTrigger;
    @Valid
    @NotNull
    private SchedulingTaskParams schedulingTaskParams;
    private String description;
}
