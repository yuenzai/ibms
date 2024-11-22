package cn.ecosync.ibms.jpa.repository;

import cn.ecosync.ibms.domain.scheduling.Scheduling;
import cn.ecosync.ibms.domain.scheduling.SchedulingId;
import cn.ecosync.ibms.domain.scheduling.SchedulingRepository;
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
