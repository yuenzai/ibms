package cn.ecosync.ibms.scheduling.repository.jpa;

import cn.ecosync.ibms.scheduling.model.*;
import cn.ecosync.ibms.scheduling.repository.SchedulingQueryModelExtension;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface SchedulingJpaRepository extends JpaRepository<Scheduling, Integer>, SchedulingRepository, SchedulingQueryModelExtension {
    @Override
    default void add(SchedulingCommandModel scheduling) {
        save((Scheduling) scheduling);
    }

    @Override
    default void remove(SchedulingCommandModel scheduling) {
        delete((Scheduling) scheduling);
    }

    @Override
    default Optional<SchedulingCommandModel> get(SchedulingId schedulingId) {
        return findBySchedulingId(schedulingId)
                .map(SchedulingCommandModel.class::cast);
    }

    @Override
    default List<SchedulingQueryModel> search(SchedulingModel probe, Sort sort) {
        return findAll(newExample(probe), sort).stream()
                .map(this::toQueryModel)
                .collect(Collectors.toList());
    }

    @Override
    default Page<SchedulingQueryModel> search(SchedulingModel probe, Pageable pageable) {
        return findAll(newExample(probe), pageable)
                .map(this::toQueryModel);
    }

    default Example<Scheduling> newExample(SchedulingModel model) {
        Scheduling probe = Scheduling.newProbe(model);
        return Example.of(probe);
    }

    Optional<Scheduling> findBySchedulingId(SchedulingId schedulingId);
}
