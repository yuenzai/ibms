package cn.ecosync.ibms.scheduling.controller;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.scheduling.dto.SchedulingStatusDto;
import cn.ecosync.ibms.scheduling.query.GetSchedulingTasksQuery;
import cn.ecosync.ibms.scheduling.query.SearchSchedulingQuery;
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

    @PostMapping
    public void execute(@RequestBody @Validated Command command) {
        commandBus.execute(command);
    }

    @GetMapping("/tasks")
    public List<String> tasks() {
        return queryBus.execute(new GetSchedulingTasksQuery());
    }

    @GetMapping
    public Iterable<SchedulingStatusDto> search(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false) Integer pageSize
    ) {
        SearchSchedulingQuery query = new SearchSchedulingQuery(page, pageSize);
        return queryBus.execute(query);
    }
}
