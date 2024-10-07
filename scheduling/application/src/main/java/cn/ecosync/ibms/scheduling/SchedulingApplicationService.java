package cn.ecosync.ibms.scheduling;

import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;

import java.util.List;

public interface SchedulingApplicationService {
    List<String> getSchedulingTasks();

    void execute(String schedulingTask);

    void schedule(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, String schedulingTask);

    void cancel(SchedulingId schedulingId);

    void pause(SchedulingId schedulingId);

    Boolean isRunning(SchedulingId schedulingId);
}
