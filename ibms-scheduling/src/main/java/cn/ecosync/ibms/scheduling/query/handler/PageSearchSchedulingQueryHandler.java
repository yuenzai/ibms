package cn.ecosync.ibms.scheduling.query.handler;

import cn.ecosync.ibms.scheduling.model.SchedulingDto;
import cn.ecosync.ibms.scheduling.model.SchedulingQueryModel;
import cn.ecosync.ibms.scheduling.repository.SchedulingRepository;
import cn.ecosync.ibms.scheduling.query.PageSearchSchedulingQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PageSearchSchedulingQueryHandler implements QueryHandler<PageSearchSchedulingQuery, Page<SchedulingQueryModel>> {
    private final SchedulingRepository schedulingRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulingQueryModel> handle(PageSearchSchedulingQuery query) {
        SchedulingDto probe = new SchedulingDto();
        return schedulingRepository.search(probe, query.toPageable());
    }
}
