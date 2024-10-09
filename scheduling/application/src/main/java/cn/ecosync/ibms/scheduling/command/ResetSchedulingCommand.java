package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class ResetSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;

    public SchedulingId toSchedulingId() {
        return new SchedulingId(this.schedulingName);
    }
}
