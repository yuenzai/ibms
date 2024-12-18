package cn.ecosync.ibms.scheduling.model;

public interface SchedulingModel {
    SchedulingId getSchedulingId();

    SchedulingTrigger getSchedulingTrigger();

    SchedulingTaskParams getSchedulingTaskParams();

    String getDescription();

    Long getCreatedDate();

    Long getLastModifiedDate();
}
