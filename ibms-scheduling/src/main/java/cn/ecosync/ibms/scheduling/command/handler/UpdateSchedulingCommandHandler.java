package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.command.UpdateSchedulingCommand;
import cn.ecosync.ibms.scheduling.model.SchedulingCommandModel;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.event.EventBus;
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
        if (command.getSchedulingTaskParams() != null) {
            schedulingApplicationService.checkExists(command.getSchedulingTaskParams());
        }

        SchedulingId schedulingId = command.getSchedulingId();
        SchedulingCommandModel scheduling = schedulingRepository.get(schedulingId).orElse(null);
        Assert.notNull(scheduling, "scheduling doesn't exist");

        Collection<Event> events = scheduling.update(command.getDescription(), command.getSchedulingTrigger(), command.getSchedulingTaskParams());
        events.forEach(eventBus::publish);
    }
}
