package cn.ecosync.ibms.scheduling.dto;

import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SchedulingDto {
    private String schedulingName;
    private SchedulingTrigger schedulingTrigger;
    private Boolean enabled;
    private Long createdDate;
    private Long lastModifiedDate;
}
