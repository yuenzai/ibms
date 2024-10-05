package cn.ecosync.ibms.scheduling.dto;

import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SchedulingDto {
    private String schedulingName;
    private SchedulingTrigger schedulingTrigger;
    private Boolean enabled;

    public SchedulingDto() {
    }

    public SchedulingDto(String schedulingName, SchedulingTrigger schedulingTrigger, Boolean enabled) {
        this.schedulingName = schedulingName;
        this.schedulingTrigger = schedulingTrigger;
        this.enabled = enabled;
    }
}
