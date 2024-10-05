package cn.ecosync.ibms.scheduling.dto;

import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SchedulingStatusDto extends SchedulingDto {
    /**
     * true or false or null
     */
    private Boolean running;

    public SchedulingStatusDto() {
    }

    public SchedulingStatusDto(String schedulingName, SchedulingTrigger schedulingTrigger, Boolean enabled) {
        super(schedulingName, schedulingTrigger, enabled);
    }
}
