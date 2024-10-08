package cn.ecosync.ibms.scheduling.dto;

import cn.ecosync.ibms.scheduling.model.SchedulingState;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString(callSuper = true)
public class SchedulingStateDto extends SchedulingDto {
    private SchedulingState schedulingState;

    public SchedulingStateDto() {
    }

    public SchedulingStateDto(String schedulingName, SchedulingTrigger schedulingTrigger, Boolean enabled, Long createdDate, Long lastModifiedDate) {
        super(schedulingName, schedulingTrigger, enabled, createdDate, lastModifiedDate);
    }

    public SchedulingState getSchedulingState() {
        return schedulingState != null ? schedulingState : SchedulingState.NONE;
    }
}
