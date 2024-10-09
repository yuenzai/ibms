package cn.ecosync.ibms.scheduling.model;

import cn.ecosync.ibms.event.Event;
import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import cn.ecosync.ibms.scheduling.event.SchedulingDisabledEvent;
import cn.ecosync.ibms.scheduling.event.SchedulingEnabledEvent;
import cn.ecosync.ibms.scheduling.event.SchedulingRescheduledEvent;
import cn.ecosync.ibms.scheduling.jpa.SchedulingTaskParamsAttributeConverter;
import cn.ecosync.ibms.scheduling.jpa.SchedulingTriggerAttributeConverter;
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
public class Scheduling extends ConcurrencySafeEntity implements AggregateRoot {
    @Embedded
    private SchedulingId schedulingId;

    @Convert(converter = SchedulingTriggerAttributeConverter.class)
    @Column(name = "scheduling_trigger", nullable = false)
    private SchedulingTrigger schedulingTrigger;

    @Convert(converter = SchedulingTaskParamsAttributeConverter.class)
    @Column(name = "scheduling_task_params", nullable = false)
    private SchedulingTaskParams schedulingTaskParams;

    private Boolean enabled;

    protected Scheduling() {
    }

    public Scheduling(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, SchedulingTaskParams schedulingTaskParams) {
        Assert.notNull(schedulingId, "schedulingId must not be null");
        Assert.notNull(schedulingTrigger, "schedulingTrigger must not be null");
        Assert.notNull(schedulingTaskParams, "schedulingTaskParams must not be empty");
        this.schedulingId = schedulingId;
        this.schedulingTrigger = schedulingTrigger;
        this.schedulingTaskParams = schedulingTaskParams;
        this.enabled = Boolean.FALSE;
    }

    public Collection<Event> enable() {
        this.enabled = Boolean.TRUE;
        return Collections.singletonList(new SchedulingEnabledEvent(this.schedulingId, this.schedulingTrigger, this.schedulingTaskParams));
    }

    public Collection<Event> disable() {
        this.enabled = Boolean.FALSE;
        return Collections.singletonList(new SchedulingDisabledEvent(this.schedulingId));
    }

    public Collection<Event> update(SchedulingTrigger schedulingTrigger, SchedulingTaskParams schedulingTaskParams) {
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

    @Override
    public String aggregateType() {
        return SchedulingConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return schedulingId.toString();
    }
}
