package cn.ecosync.ibms.scheduling.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;

@ToString
public class SchedulingDto implements SchedulingModel {
    @JsonUnwrapped
    private SchedulingId schedulingId;
    private SchedulingTrigger schedulingTrigger;
    private SchedulingTaskParams schedulingTaskParams;
    private String description;
    private Long createdDate;
    private Long lastModifiedDate;

    @Override
    public SchedulingId getSchedulingId() {
        return schedulingId;
    }

    @Override
    public SchedulingTrigger getSchedulingTrigger() {
        return schedulingTrigger;
    }

    @Override
    public SchedulingTaskParams getSchedulingTaskParams() {
        return schedulingTaskParams;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Long getCreatedDate() {
        return createdDate;
    }

    @Override
    public Long getLastModifiedDate() {
        return lastModifiedDate;
    }
}
