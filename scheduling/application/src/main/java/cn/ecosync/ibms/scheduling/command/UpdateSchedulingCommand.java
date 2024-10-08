package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
public class UpdateSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
    @Valid
    private SchedulingTrigger schedulingTrigger;
    @Size(min = 1)
    private String schedulingTask;

    public SchedulingId toSchedulingId() {
        return new SchedulingId(this.schedulingName);
    }
}
