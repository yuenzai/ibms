package cn.ecosync.ibms.scheduling.repository.jpa;

import cn.ecosync.ibms.scheduling.model.Scheduling;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchedulingJpaRepository extends JpaRepository<Scheduling, Integer>, SchedulingRepository {
    @Override
    default void add(Scheduling scheduling) {
        save(scheduling);
    }

    @Override
    default void remove(Scheduling scheduling) {
        delete(scheduling);
    }

    @Override
    default Optional<Scheduling> get(SchedulingId schedulingId) {
        return findBySchedulingId(schedulingId);
    }

    Optional<Scheduling> findBySchedulingId(SchedulingId schedulingId);
}
