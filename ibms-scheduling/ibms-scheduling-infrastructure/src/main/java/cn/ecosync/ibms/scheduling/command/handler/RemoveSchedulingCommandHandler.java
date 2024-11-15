package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.scheduling.application.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.command.RemoveSchedulingCommand;
import cn.ecosync.ibms.scheduling.model.Scheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class RemoveSchedulingCommandHandler implements CommandHandler<RemoveSchedulingCommand> {
    private final SchedulingRepository schedulingRepository;
    private final SchedulingApplicationService schedulingApplicationService;

    @Override
    @Transactional
    public void handle(RemoveSchedulingCommand command) {
        SchedulingId schedulingId = new SchedulingId(command.getSchedulingName());
        Scheduling scheduling = schedulingRepository.get(schedulingId).orElse(null);
        Assert.notNull(scheduling, "scheduling not found:" + schedulingId);
        schedulingApplicationService.cancel(schedulingId);
        schedulingRepository.remove(scheduling);
    }
}
