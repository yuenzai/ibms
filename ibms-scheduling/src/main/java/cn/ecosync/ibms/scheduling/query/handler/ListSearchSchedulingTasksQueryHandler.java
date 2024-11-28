package cn.ecosync.ibms.scheduling.query.handler;

import cn.ecosync.ibms.scheduling.domain.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.query.ListSearchSchedulingTasksQuery;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListSearchSchedulingTasksQueryHandler implements QueryHandler<ListSearchSchedulingTasksQuery, List<String>> {
    private final SchedulingApplicationService schedulingApplicationService;

    @Override
    @Transactional(readOnly = true)
    public List<String> handle(ListSearchSchedulingTasksQuery query) {
        return schedulingApplicationService.getSchedulingTasks();
    }
}
