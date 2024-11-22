package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.scheduling.dto.SchedulingTaskParams;
import cn.ecosync.ibms.scheduling.dto.SchedulingTrigger;
import cn.ecosync.iframework.command.Command;
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
