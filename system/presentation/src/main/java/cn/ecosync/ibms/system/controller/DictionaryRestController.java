package cn.ecosync.ibms.system.controller;

import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.system.command.PutDictionaryCommand;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.system.model.DictionaryValue;
import cn.ecosync.ibms.system.query.GetDictionaryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionary")
public class DictionaryRestController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping
    public void put(@RequestBody @Validated PutDictionaryCommand command) {
        commandBus.execute(command);
    }

    @GetMapping("/{dictKey}")
    public DictionaryValue get(@PathVariable String dictKey) {
        return queryBus.execute(new GetDictionaryQuery(new DictionaryKey(dictKey))).orElse(null);
    }
}
