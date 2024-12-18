package cn.ecosync.ibms.scheduling.model;

import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;

import java.util.List;

@ToString
public class SchedulingQueryModelDto implements SchedulingQueryModel {
    @JsonUnwrapped
    private SchedulingModel scheduling;

    private SchedulingState schedulingState;
    private List<Long> nextFireTimes;
    private Long previousFireTime;

    protected SchedulingQueryModelDto() {
    }

    public SchedulingQueryModelDto(SchedulingModel scheduling, SchedulingState schedulingState, List<Long> nextFireTimes, Long previousFireTime) {
        this.scheduling = scheduling;
        this.schedulingState = schedulingState;
        this.nextFireTimes = nextFireTimes;
        this.previousFireTime = previousFireTime;
    }

    @Override
    public SchedulingState getSchedulingState() {
        return schedulingState != null ? schedulingState : SchedulingState.NONE;
    }

    @Override
    public List<Long> getNextFireTimes() {
        return CollectionUtils.nullSafeOf(nextFireTimes);
    }

    @Override
    public Long getPreviousFireTime() {
        return previousFireTime;
    }

    @Override
    public SchedulingId getSchedulingId() {
        return scheduling.getSchedulingId();
    }

    @Override
    public SchedulingTrigger getSchedulingTrigger() {
        return scheduling.getSchedulingTrigger();
    }

    @Override
    public SchedulingTaskParams getSchedulingTaskParams() {
        return scheduling.getSchedulingTaskParams();
    }

    @Override
    public String getDescription() {
        return scheduling.getDescription();
    }

    @Override
    public Long getCreatedDate() {
        return scheduling.getCreatedDate();
    }

    @Override
    public Long getLastModifiedDate() {
        return scheduling.getLastModifiedDate();
    }
}
