package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class UpdateSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
    @Valid
    private SchedulingTrigger schedulingTrigger;
    @Valid
    private SchedulingTaskParams schedulingTaskParams;
    private String description;
}
