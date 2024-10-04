package cn.ecosync.ibms.scheduling.model;

import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.ConcurrencySafeEntity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scheduling")
public class Scheduling extends ConcurrencySafeEntity implements AggregateRoot {
    @Embedded
    private SchedulingId schedulingId;

    @Override
    public String aggregateType() {
        return SchedulingConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return schedulingId.toString();
    }
}
