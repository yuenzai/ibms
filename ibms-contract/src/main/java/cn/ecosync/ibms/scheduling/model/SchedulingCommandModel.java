package cn.ecosync.ibms.scheduling.model;

import cn.ecosync.iframework.event.Event;

import java.util.Collection;

public interface SchedulingCommandModel extends SchedulingModel {
    Collection<Event> update(String description, SchedulingTrigger schedulingTrigger, SchedulingTaskParams schedulingTaskParams);
}
