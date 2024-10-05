package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingTask;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class AddSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
    @NotNull
    private SchedulingTrigger schedulingTrigger;
    @NotNull
    private SchedulingTask schedulingTask;

    public SchedulingId toSchedulingId() {
        return new SchedulingId(this.schedulingName);
    }
}
