package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.command.ResetSchedulingCommand;
import cn.ecosync.ibms.scheduling.domain.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.domain.SchedulingId;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ResetSchedulingCommandHandler implements CommandHandler<ResetSchedulingCommand> {
    private final SchedulingApplicationService schedulingApplicationService;

    @Override
    @Transactional
    public void handle(ResetSchedulingCommand command) {
        SchedulingId schedulingId = new SchedulingId(command.getSchedulingName());
        schedulingApplicationService.resetError(schedulingId);
    }
}
