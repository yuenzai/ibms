package cn.ecosync.ibms.scheduling.repository;

import cn.ecosync.ibms.scheduling.model.SchedulingCommandModel;
import cn.ecosync.ibms.scheduling.model.SchedulingId;
import cn.ecosync.ibms.scheduling.model.SchedulingModel;
import cn.ecosync.ibms.scheduling.model.SchedulingQueryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface SchedulingRepository {
    void add(SchedulingCommandModel scheduling);

    void remove(SchedulingCommandModel scheduling);

    Optional<SchedulingCommandModel> get(SchedulingId schedulingId);

    List<SchedulingQueryModel> search(SchedulingModel probe, Sort sort);

    Page<SchedulingQueryModel> search(SchedulingModel probe, Pageable pageable);
}
