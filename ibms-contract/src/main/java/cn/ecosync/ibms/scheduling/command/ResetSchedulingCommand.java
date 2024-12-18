package cn.ecosync.ibms.scheduling.command;

import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.iframework.command.Command;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResetSchedulingCommand implements Command {
    @Valid
    @JsonUnwrapped
    private SchedulingId schedulingId;
}
