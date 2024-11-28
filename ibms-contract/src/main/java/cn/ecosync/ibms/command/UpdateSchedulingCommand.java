package cn.ecosync.ibms.command;

import cn.ecosync.ibms.dto.SchedulingTaskParams;
import cn.ecosync.ibms.dto.SchedulingTrigger;
import cn.ecosync.iframework.command.Command;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

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
