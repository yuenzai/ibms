package cn.ecosync.ibms.scheduling.model;

import cn.ecosync.iframework.util.ToStringId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

import java.util.Objects;

@Embeddable
public class SchedulingId implements ToStringId {
    @Column(name = "scheduling_name", nullable = false, updatable = false)
    private String schedulingName;

    protected SchedulingId() {
    }

    public SchedulingId(String schedulingName) {
        Assert.hasText(schedulingName, "schedulingName must not be empty");
        this.schedulingName = schedulingName;
    }

    protected String getSchedulingName() {
        return schedulingName;
    }

    @Override
    public String toString() {
        return toStringId();
    }

    @Override
    public String toStringId() {
        return schedulingName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SchedulingId)) return false;
        SchedulingId that = (SchedulingId) o;
        return Objects.equals(schedulingName, that.schedulingName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(schedulingName);
    }
}
