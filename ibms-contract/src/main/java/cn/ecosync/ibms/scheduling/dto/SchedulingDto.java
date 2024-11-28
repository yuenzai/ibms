package cn.ecosync.ibms.scheduling.dto;

import cn.ecosync.iframework.util.CollectionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SchedulingDto {
    private String schedulingName;
    private SchedulingTrigger schedulingTrigger;
    private SchedulingTaskParams schedulingTaskParams;
    private String description;
    private Long createdDate;
    private Long lastModifiedDate;

    private SchedulingState schedulingState;
    private List<Long> nextFireTimes;
    private Long previousFireTime;

    protected SchedulingDto() {
    }

    public SchedulingDto(String schedulingName, SchedulingTrigger schedulingTrigger, SchedulingTaskParams schedulingTaskParams, String description, Long createdDate, Long lastModifiedDate) {
        this.schedulingName = schedulingName;
        this.schedulingTrigger = schedulingTrigger;
        this.schedulingTaskParams = schedulingTaskParams;
        this.description = description;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public SchedulingState getSchedulingState() {
        return schedulingState != null ? schedulingState : SchedulingState.NONE;
    }

    public List<Long> getNextFireTimes() {
        return CollectionUtils.nullSafeOf(nextFireTimes);
    }
}
