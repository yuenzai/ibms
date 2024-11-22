package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.domain.scheduling.Scheduling;
import cn.ecosync.ibms.domain.scheduling.SchedulingId;
import cn.ecosync.ibms.domain.scheduling.SchedulingRepository;
import cn.ecosync.ibms.domain.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.command.AddSchedulingCommand;
import cn.ecosync.iframework.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class AddSchedulingCommandHandler implements CommandHandler<AddSchedulingCommand> {
    private final SchedulingRepository schedulingRepository;
    private final SchedulingApplicationService schedulingApplicationService;

    @Override
    @Transactional
    public void handle(AddSchedulingCommand command) {
        schedulingApplicationService.checkExists(command.getSchedulingTaskParams());

        SchedulingId schedulingId = new SchedulingId(command.getSchedulingName());
        Scheduling scheduling = schedulingRepository.get(schedulingId).orElse(null);
        Assert.isNull(scheduling, "schedulingName already exists");

        scheduling = new Scheduling(schedulingId, command.getSchedulingTrigger(), command.getSchedulingTaskParams(), command.getDescription());
        schedulingRepository.add(scheduling);
    }
}
