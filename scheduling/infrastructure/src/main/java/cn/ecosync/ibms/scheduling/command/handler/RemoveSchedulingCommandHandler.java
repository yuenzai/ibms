package cn.ecosync.ibms.scheduling.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.scheduling.SchedulingApplicationService;
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
        SchedulingId schedulingId = command.toSchedulingId();
        Scheduling scheduling = schedulingRepository.get(schedulingId).orElse(null);
        Assert.notNull(scheduling, "Scheduling not found");
        // 先从数据库修改为禁用状态
        if (scheduling.getEnabled()) {
            throw new IllegalStateException("Please disable scheduling first");
        }
        schedulingApplicationService.cancel(schedulingId);
        schedulingRepository.remove(scheduling);
    }
}
