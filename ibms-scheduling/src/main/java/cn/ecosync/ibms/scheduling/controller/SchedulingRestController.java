package cn.ecosync.ibms.scheduling.controller;

import cn.ecosync.ibms.scheduling.command.*;
import cn.ecosync.ibms.scheduling.model.SchedulingQueryModel;
import cn.ecosync.ibms.scheduling.query.ListSearchSchedulingQuery;
import cn.ecosync.ibms.scheduling.query.ListSearchSchedulingTasksQuery;
import cn.ecosync.ibms.scheduling.query.PageSearchSchedulingQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scheduling")
public class SchedulingRestController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/add")
    public void execute(@RequestBody @Validated AddSchedulingCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/update")
    public void execute(@RequestBody @Validated UpdateSchedulingCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveSchedulingCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/switch")
    public void execute(@RequestBody @Validated SwitchSchedulingCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/reset")
    public void execute(@RequestBody @Validated ResetSchedulingCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/task/list-search")
    public List<String> tasks(@RequestBody @Validated ListSearchSchedulingTasksQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/list-search")
    public List<SchedulingQueryModel> search(@RequestBody @Validated ListSearchSchedulingQuery query) {
        return queryBus.execute(query);
    }

    @PostMapping("/page-search")
    public Page<SchedulingQueryModel> search(@RequestBody @Validated PageSearchSchedulingQuery query) {
        return queryBus.execute(query);
    }
}
