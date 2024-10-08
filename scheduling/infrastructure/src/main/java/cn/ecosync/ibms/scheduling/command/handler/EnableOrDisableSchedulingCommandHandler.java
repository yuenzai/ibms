package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.event.Event;
import cn.ecosync.ibms.event.EventBus;
import cn.ecosync.ibms.scheduling.command.EnableOrDisableSchedulingCommand;
import cn.ecosync.ibms.scheduling.model.Scheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class EnableOrDisableSchedulingCommandHandler implements CommandHandler<EnableOrDisableSchedulingCommand> {
    private final SchedulingRepository schedulingRepository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void handle(EnableOrDisableSchedulingCommand command) {
        SchedulingId schedulingId = command.toSchedulingId();
        Scheduling scheduling = schedulingRepository.get(schedulingId).orElse(null);
        if (scheduling != null) {
            Collection<Event> events = command.getEnabled() ? scheduling.enable() : scheduling.disable();
            events.forEach(eventBus::publish);
        }
    }
}
