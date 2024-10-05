package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.scheduling.command.AddSchedulingCommand;
import cn.ecosync.ibms.scheduling.model.Scheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AddSchedulingCommandHandler implements CommandHandler<AddSchedulingCommand> {
    private final SchedulingRepository schedulingRepository;

    @Override
    @Transactional
    public void handle(AddSchedulingCommand command) {
        SchedulingId schedulingId = command.toSchedulingId();
        Scheduling scheduling = schedulingRepository.get(schedulingId).orElse(null);
        if (scheduling == null) {
            scheduling = new Scheduling(schedulingId, command.getSchedulingTrigger(), command.getSchedulingTask());
            schedulingRepository.add(scheduling);
        } else {
            throw new IllegalArgumentException("scheduling name already exists");
        }
    }
}
