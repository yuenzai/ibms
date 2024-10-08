package cn.ecosync.ibms.scheduling;

import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingState;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface SchedulingApplicationService {
    List<String> getSchedulingTasks();

    void execute(String schedulingTask);

    default void schedule(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, String schedulingTask) {
        schedule(schedulingId, schedulingTrigger, schedulingTask, Collections.emptyMap());
    }

    void schedule(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, String schedulingTask, Map<String, Object> schedulingTaskParams);

    void cancel(SchedulingId schedulingId);

    void pause(SchedulingId schedulingId);

    SchedulingState getSchedulingState(SchedulingId schedulingId);

    void existsBy(String schedulingTask);
}
