package cn.ecosync.ibms.scheduling.query.handler;

import cn.ecosync.ibms.scheduling.model.SchedulingDto;
import cn.ecosync.ibms.scheduling.model.SchedulingQueryModel;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import cn.ecosync.ibms.scheduling.query.ListSearchSchedulingQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListSearchSchedulingQueryHandler implements QueryHandler<ListSearchSchedulingQuery, List<SchedulingQueryModel>> {
    private final SchedulingRepository schedulingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SchedulingQueryModel> handle(ListSearchSchedulingQuery query) {
        SchedulingDto probe = new SchedulingDto();
        return schedulingRepository.search(probe, query.toSort());
    }
}
