package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.command.AddSchedulingCommand;
import cn.ecosync.ibms.scheduling.model.Scheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingCommandModel;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
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

        SchedulingCommandModel scheduling = schedulingRepository.get(command.getSchedulingId()).orElse(null);
        Assert.isNull(scheduling, "schedulingName already exists");

        scheduling = new Scheduling(command.getSchedulingId(), command.getSchedulingTrigger(), command.getSchedulingTaskParams(), command.getDescription());
        schedulingRepository.add(scheduling);
    }
}
