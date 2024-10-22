package cn.ecosync.ibms.scheduling.dto;

import cn.ecosync.ibms.scheduling.model.SchedulingState;
import cn.ecosync.ibms.scheduling.model.SchedulingTaskParams;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import cn.ecosync.ibms.util.CollectionUtils;
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

    public SchedulingState getSchedulingState() {
        return schedulingState != null ? schedulingState : SchedulingState.NONE;
    }

    public List<Long> getNextFireTimes() {
        return CollectionUtils.nullSafeOf(nextFireTimes);
    }
}
