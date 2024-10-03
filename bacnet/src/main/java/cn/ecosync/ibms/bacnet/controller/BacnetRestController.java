package cn.ecosync.ibms.bacnet.controller;

import cn.ecosync.ibms.bacnet.BacnetApplicationService;
import cn.ecosync.ibms.bacnet.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bacnet")
public class BacnetRestController {
    private final BacnetApplicationService bacnetApplicationService;

    @PostMapping("/readpropm")
    public List<ReadPropertyMultipleAck> readpropm(@RequestBody @Validated BacnetReadPropertyMultipleQuery query) {
        try {
            return bacnetApplicationService.readpropm(query);
        } catch (Exception e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }
}
