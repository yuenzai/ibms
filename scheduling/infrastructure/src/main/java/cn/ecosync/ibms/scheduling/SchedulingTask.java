package cn.ecosync.ibms.scheduling;

import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;

public interface SchedulingTask<TP extends SchedulingTaskParams> {
    String taskId();

    Class<TP> taskParamsType();
}
