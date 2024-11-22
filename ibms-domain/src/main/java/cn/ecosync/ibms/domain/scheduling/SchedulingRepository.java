package cn.ecosync.ibms.domain.scheduling;

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
