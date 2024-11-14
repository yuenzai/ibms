package cn.ecosync.ibms.scheduling.query.handler;

import cn.ecosync.ibms.query.QueryHandler;
import cn.ecosync.ibms.scheduling.application.SchedulingApplicationService;
import cn.ecosync.ibms.scheduling.query.GetSchedulingTasksQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetSchedulingTasksQueryHandler implements QueryHandler<GetSchedulingTasksQuery, List<String>> {
    private final SchedulingApplicationService schedulingApplicationService;

    @Override
    @Transactional(readOnly = true)
    public List<String> handle(GetSchedulingTasksQuery query) {
        return schedulingApplicationService.getSchedulingTasks();
    }
}
