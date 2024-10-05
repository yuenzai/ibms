package cn.ecosync.ibms.scheduling;

import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingTask;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;

public interface SchedulingApplicationService {
    void execute(SchedulingTask schedulingTask);

    void executeAsync(SchedulingTask schedulingTask);

    void schedule(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, SchedulingTask schedulingTask);

    void cancel(SchedulingId schedulingId);

    Boolean isRunning(SchedulingId schedulingId);
}
