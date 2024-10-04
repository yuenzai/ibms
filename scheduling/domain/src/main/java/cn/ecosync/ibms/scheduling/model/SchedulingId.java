package cn.ecosync.ibms.scheduling.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@EqualsAndHashCode
public class SchedulingId {
    protected SchedulingId() {
    }
}
