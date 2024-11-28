package cn.ecosync.ibms.scheduling.query.handler;

import cn.ecosync.ibms.scheduling.domain.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.domain.SchedulingReadonlyRepository;
import cn.ecosync.ibms.scheduling.dto.SchedulingDto;
import cn.ecosync.ibms.scheduling.query.PageSearchSchedulingQuery;
import cn.ecosync.iframework.query.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PageSearchSchedulingQueryHandler extends SearchSchedulingQueryHandler implements QueryHandler<PageSearchSchedulingQuery, Page<SchedulingDto>> {
    private final SchedulingReadonlyRepository schedulingReadonlyRepository;

    public PageSearchSchedulingQueryHandler(SchedulingApplicationService schedulingApplicationService, SchedulingReadonlyRepository schedulingReadonlyRepository) {
        super(schedulingApplicationService);
        this.schedulingReadonlyRepository = schedulingReadonlyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulingDto> handle(PageSearchSchedulingQuery query) {
        Pageable pageable = query.toPageable();
        Page<SchedulingDto> schedules = schedulingReadonlyRepository.pageSearch(pageable);
        putStateFor(schedules, query.getMaxCount());
        return schedules;
    }
}
