package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.command.SwitchSchedulingCommand;
import cn.ecosync.ibms.scheduling.model.SchedulingCommandModel;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
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
        SchedulingId schedulingId = command.getSchedulingId();
        SchedulingCommandModel scheduling = schedulingRepository.get(schedulingId).orElse(null);
        Assert.notNull(scheduling, "scheduling cannot be null");
        if (command.getEnabled()) {
            schedulingApplicationService.schedule(scheduling.getSchedulingId(), scheduling.getSchedulingTrigger(), scheduling.getSchedulingTaskParams());
        } else {
            schedulingApplicationService.pause(schedulingId);
        }
    }
}
