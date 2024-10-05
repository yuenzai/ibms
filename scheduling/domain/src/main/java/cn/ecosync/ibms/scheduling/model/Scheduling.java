package cn.ecosync.ibms.scheduling.model;

import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;
import cn.ecosync.ibms.scheduling.event.SchedulingDisabledEvent;
import cn.ecosync.ibms.scheduling.event.SchedulingEnabledEvent;
import cn.ecosync.ibms.scheduling.jpa.SchedulingTaskAttributeConverter;
import cn.ecosync.ibms.scheduling.jpa.SchedulingTriggerAttributeConverter;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.*;

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

    @Convert(converter = SchedulingTaskAttributeConverter.class)
    @Column(name = "scheduling_task", nullable = false)
    private SchedulingTask schedulingTask;

    private Boolean enabled;

    protected Scheduling() {
    }

    public Scheduling(SchedulingId schedulingId, SchedulingTrigger schedulingTrigger, SchedulingTask schedulingTask) {
        Assert.notNull(schedulingId, "schedulingId must not be null");
        Assert.notNull(schedulingTrigger, "schedulingTrigger must not be null");
        Assert.notNull(schedulingTask, "schedulingTask must not be null");
        this.schedulingId = schedulingId;
        this.schedulingTrigger = schedulingTrigger;
        this.schedulingTask = schedulingTask;
        this.enabled = Boolean.FALSE;
    }

    public SchedulingEnabledEvent enable() {
        this.enabled = Boolean.TRUE;
        return new SchedulingEnabledEvent(this.schedulingId, this.schedulingTrigger, this.schedulingTask);
    }

    public SchedulingDisabledEvent disable() {
        this.enabled = Boolean.FALSE;
        return new SchedulingDisabledEvent(this.schedulingId);
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
