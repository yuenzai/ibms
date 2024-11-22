package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.scheduling.dto.SchedulingTaskParams;
import cn.ecosync.ibms.scheduling.dto.SchedulingTrigger;
import cn.ecosync.iframework.command.Command;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
