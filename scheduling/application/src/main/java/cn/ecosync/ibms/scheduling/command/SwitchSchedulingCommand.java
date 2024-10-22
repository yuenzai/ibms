package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class SwitchSchedulingCommand implements Command {
    @NotBlank
    private String schedulingName;
    @NotNull
    private Boolean enabled;

    public SchedulingId toSchedulingId() {
        return new SchedulingId(this.schedulingName);
    }
}
