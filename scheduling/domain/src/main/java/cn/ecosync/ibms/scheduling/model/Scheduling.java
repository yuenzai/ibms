package cn.ecosync.ibms.scheduling.model;

import cn.ecosync.ibms.event.Event;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import cn.ecosync.ibms.scheduling.event.SchedulingRescheduledEvent;
import cn.ecosync.ibms.scheduling.jpa.SchedulingTaskParamsAttributeConverter;
import cn.ecosync.ibms.scheduling.jpa.SchedulingTriggerAttributeConverter;
import cn.ecosync.ibms.util.StringUtils;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

/**
 * 任务计划安排
 *
 * @author yuenzai
 * @since 2024
 */
@Entity
@Table(name = "scheduling")
@Getter
public class Scheduling extends ConcurrencySafeEntity {
    @Embedded
    private SchedulingId schedulingId;

    @Convert(converter = SchedulingTriggerAttributeConverter.class)
    @Column(name = "scheduling_trigger", nullable = false)
    private SchedulingTrigger schedulingTrigger;

    @Convert(converter = SchedulingTaskParamsAttributeConverter.class)
    @Column(name = "scheduling_task_params", nullable = false)
    private SchedulingTaskParams schedulingTaskParams;

    @Column(name = "description", nullable = false)
    private String description = "";

    protected Scheduling() {
    }

    public Scheduling(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, SchedulingTaskParams schedulingTaskParams, String description) {
        Assert.notNull(schedulingId, "schedulingId must not be null");
        Assert.notNull(schedulingTrigger, "schedulingTrigger must not be null");
        Assert.notNull(schedulingTaskParams, "schedulingTaskParams must not be empty");
        this.schedulingId = schedulingId;
        this.schedulingTrigger = schedulingTrigger;
        this.schedulingTaskParams = schedulingTaskParams;
        this.description = StringUtils.nullSafeOf(description);
    }

    public Collection<Event> update(String description, SchedulingTrigger schedulingTrigger, SchedulingTaskParams schedulingTaskParams) {
        if (description != null) {
            this.description = description;
        }
        if (schedulingTrigger == null && schedulingTaskParams == null) {
            return Collections.emptyList();
        }
        if (schedulingTrigger != null) {
            this.schedulingTrigger = schedulingTrigger;
        }
        if (schedulingTaskParams != null) {
            this.schedulingTaskParams = schedulingTaskParams;
        }
        return Collections.singletonList(new SchedulingRescheduledEvent(this.schedulingId, this.schedulingTrigger, this.schedulingTaskParams));
    }
}
