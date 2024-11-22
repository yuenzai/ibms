package cn.ecosync.ibms.command.handler;

import cn.ecosync.ibms.domain.Scheduling;
import cn.ecosync.ibms.domain.SchedulingId;
import cn.ecosync.ibms.domain.SchedulingRepository;
import cn.ecosync.ibms.domain.SchedulingApplicationService;
import cn.ecosync.ibms.command.SwitchSchedulingCommand;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class SwitchSchedulingCommandHandler implements CommandHandler<SwitchSchedulingCommand> {
    private final SchedulingApplicationService schedulingApplicationService;
    private final SchedulingRepository schedulingRepository;

    @Override
    @Transactional
    public void handle(SwitchSchedulingCommand command) {
        SchedulingId schedulingId = new SchedulingId(command.getSchedulingName());
        Scheduling scheduling = schedulingRepository.get(schedulingId).orElse(null);
        Assert.notNull(scheduling, "scheduling cannot be null");
        if (command.getEnabled()) {
            schedulingApplicationService.schedule(scheduling.getSchedulingId(), scheduling.getSchedulingTrigger(), scheduling.getSchedulingTaskParams());
        } else {
            schedulingApplicationService.pause(schedulingId);
        }
    }
}
