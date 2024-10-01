package cn.ecosync.ibms.bacnet.controller;

import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.query.QueryBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bacnet")
public class BacnetRestController {
    private final QueryBus queryBus;

    @PostMapping("/readpropm")
    public Object readpropm(@RequestBody @Validated BacnetReadPropertyMultipleQuery query) {
        return queryBus.execute(query);
    }
}
