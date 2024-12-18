package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.iframework.command.Command;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SwitchSchedulingCommand implements Command {
    @Valid
    @JsonUnwrapped
    private SchedulingId schedulingId;
    @NotNull
    private Boolean enabled;
}
