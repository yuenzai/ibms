package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.command.ResetSchedulingCommand;
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
        schedulingApplicationService.resetError(command.toSchedulingId());
    }
}
