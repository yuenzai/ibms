package cn.ecosync.ibms.domain;

import cn.ecosync.ibms.dto.SchedulingState;
import cn.ecosync.ibms.dto.SchedulingTaskParams;
import cn.ecosync.ibms.dto.SchedulingTrigger;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SchedulingApplicationService {
    List<String> getSchedulingTasks();

    void execute(SchedulingTaskParams schedulingTaskParams);

    void schedule(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, SchedulingTaskParams schedulingTaskParams);

    void cancel(SchedulingId schedulingId);

    void pause(SchedulingId schedulingId);

    void resetError(SchedulingId schedulingId);

    SchedulingState getSchedulingState(SchedulingId schedulingId);

    void checkExists(SchedulingTaskParams schedulingTaskParams);

    List<Date> computeNextFireTimes(SchedulingId schedulingId, int maxCount);

    Optional<Date> getPreviousFireTime(SchedulingId schedulingId);
}
