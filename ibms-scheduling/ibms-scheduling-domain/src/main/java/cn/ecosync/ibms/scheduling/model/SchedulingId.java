package cn.ecosync.ibms.scheduling.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@EqualsAndHashCode
public class SchedulingId {
    @Column(name = "scheduling_name", nullable = false, updatable = false)
    private String schedulingName;

    protected SchedulingId() {
    }

    public SchedulingId(String schedulingName) {
        Assert.hasText(schedulingName, "schedulingName can not be empty");
        this.schedulingName = schedulingName;
    }

    @Override
    public String toString() {
        return schedulingName;
    }
}
