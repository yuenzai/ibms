package cn.ecosync.ibms.scheduling.domain;

import cn.ecosync.ibms.dto.SchedulingTaskParams;
import cn.ecosync.ibms.dto.SchedulingTrigger;
import cn.ecosync.ibms.event.SchedulingRescheduledEvent;
import cn.ecosync.iframework.domain.ConcurrencySafeEntity;
import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.util.Assert;

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

    @Column(name = "scheduling_trigger", nullable = false)
    private SchedulingTrigger schedulingTrigger;

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
        return Collections.singletonList(new SchedulingRescheduledEvent(this.schedulingId.getSchedulingName(), this.schedulingTrigger, this.schedulingTaskParams));
    }
}
