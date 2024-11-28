package cn.ecosync.ibms.scheduling.controller;

import cn.ecosync.ibms.scheduling.command.*;
import cn.ecosync.ibms.scheduling.dto.SchedulingDto;
import cn.ecosync.ibms.scheduling.query.GetSchedulingTasksQuery;
import cn.ecosync.ibms.scheduling.query.SearchSchedulingQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/tasks")
    public List<String> tasks() {
        return queryBus.execute(new GetSchedulingTasksQuery());
    }

    @GetMapping
    public Iterable<SchedulingDto> search(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false) Integer pageSize,
            @RequestParam(value = "max-count", defaultValue = "5") Integer maxCount
    ) {
        SearchSchedulingQuery query = new SearchSchedulingQuery(page, pageSize, maxCount);
        return queryBus.execute(query);
    }
}
