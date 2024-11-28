package cn.ecosync.ibms.scheduling.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

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
