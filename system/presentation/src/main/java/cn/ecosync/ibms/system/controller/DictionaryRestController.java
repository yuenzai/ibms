package cn.ecosync.ibms.system.controller;

import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.query.QueryBus;
import cn.ecosync.ibms.system.command.PutDictionaryCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
