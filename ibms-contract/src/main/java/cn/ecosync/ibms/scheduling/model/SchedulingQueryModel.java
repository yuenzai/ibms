package cn.ecosync.ibms.scheduling.model;

import java.util.List;

public interface SchedulingQueryModel extends SchedulingModel {
    SchedulingState getSchedulingState();

    List<Long> getNextFireTimes();

    Long getPreviousFireTime();
}
