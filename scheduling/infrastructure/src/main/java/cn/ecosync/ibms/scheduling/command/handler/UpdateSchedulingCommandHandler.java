package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.event.Event;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.command.UpdateSchedulingCommand;
import cn.ecosync.ibms.scheduling.model.Scheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class UpdateSchedulingCommandHandler implements CommandHandler<UpdateSchedulingCommand> {
    private final SchedulingRepository schedulingRepository;
    private final SchedulingApplicationService schedulingApplicationService;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(UpdateSchedulingCommand command) {
        schedulingApplicationService.existsBy(command.getSchedulingTask());

        SchedulingId schedulingId = command.toSchedulingId();
        Scheduling scheduling = schedulingRepository.get(schedulingId).orElse(null);
        Assert.notNull(scheduling, "Scheduling doesn't exist");

        Collection<Event> events = scheduling.update(command.getSchedulingTrigger(), command.getSchedulingTask());
        events.forEach(eventBus::publish);
    }
}
