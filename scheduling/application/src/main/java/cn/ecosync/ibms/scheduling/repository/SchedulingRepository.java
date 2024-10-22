package cn.ecosync.ibms.scheduling.repository;

import cn.ecosync.ibms.scheduling.model.Scheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface SchedulingRepository {
    void add(Scheduling scheduling);

    void remove(Scheduling scheduling);

    default void remove(SchedulingId schedulingId) {
        get(schedulingId).ifPresent(this::remove);
    }

    Optional<Scheduling> get(SchedulingId schedulingId);
}
