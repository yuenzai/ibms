package cn.ecosync.ibms.scheduling.query.handler;

import cn.ecosync.ibms.scheduling.domain.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.domain.SchedulingReadonlyRepository;
import cn.ecosync.ibms.scheduling.dto.SchedulingDto;
import cn.ecosync.ibms.scheduling.query.ListSearchSchedulingQuery;
import cn.ecosync.iframework.query.QueryHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ListSearchSchedulingQueryHandler extends SearchSchedulingQueryHandler implements QueryHandler<ListSearchSchedulingQuery, List<SchedulingDto>> {
    private final SchedulingReadonlyRepository schedulingReadonlyRepository;

    public ListSearchSchedulingQueryHandler(SchedulingApplicationService schedulingApplicationService, SchedulingReadonlyRepository schedulingReadonlyRepository) {
        super(schedulingApplicationService);
        this.schedulingReadonlyRepository = schedulingReadonlyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchedulingDto> handle(ListSearchSchedulingQuery query) {
        List<SchedulingDto> schedules = schedulingReadonlyRepository.listSearch();
        putStateFor(schedules, query.getMaxCount());
        return schedules;
    }
}
