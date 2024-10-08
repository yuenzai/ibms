package cn.ecosync.ibms.scheduling.model;

import cn.ecosync.ibms.event.Event;
import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import cn.ecosync.ibms.scheduling.event.SchedulingDisabledEvent;
import cn.ecosync.ibms.scheduling.event.SchedulingEnabledEvent;
import cn.ecosync.ibms.scheduling.event.SchedulingRescheduledEvent;
import cn.ecosync.ibms.scheduling.jpa.SchedulingTriggerAttributeConverter;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

/**
 * 任务计划安排
 * 不应限制其实现方式，除了简单的线程池实现外，还可以考虑分布式调度程序
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

    @Column(name = "scheduling_task", nullable = false)
    private String schedulingTask;

    private Boolean enabled;

    protected Scheduling() {
    }

    public Scheduling(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, String schedulingTask) {
        Assert.notNull(schedulingId, "schedulingId must not be null");
        Assert.notNull(schedulingTrigger, "schedulingTrigger must not be null");
        Assert.hasText(schedulingTask, "schedulingTask must not be empty");
        this.schedulingId = schedulingId;
        this.schedulingTrigger = schedulingTrigger;
        this.schedulingTask = schedulingTask;
        this.enabled = Boolean.FALSE;
    }

    public Collection<Event> enable() {
        this.enabled = Boolean.TRUE;
        return Collections.singletonList(new SchedulingEnabledEvent(this.schedulingId, this.schedulingTrigger, this.schedulingTask));
    }

    public Collection<Event> disable() {
        this.enabled = Boolean.FALSE;
        return Collections.singletonList(new SchedulingDisabledEvent(this.schedulingId));
    }

    public Collection<Event> update(SchedulingTrigger schedulingTrigger, String schedulingTask) {
        if (schedulingTrigger == null && schedulingTask == null) {
            return Collections.emptyList();
        }
        if (schedulingTrigger != null) {
            this.schedulingTrigger = schedulingTrigger;
        }
        if (schedulingTask != null) {
            this.schedulingTask = schedulingTask;
        }
        return Collections.singletonList(new SchedulingRescheduledEvent(this.schedulingId, this.schedulingTrigger, this.schedulingTask));
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
