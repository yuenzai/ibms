package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.BacnetApplicationService;
import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
import cn.ecosync.ibms.device.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BacnetReadPropertyMultipleQueryHandler implements QueryHandler<BacnetReadPropertyMultipleQuery, List<ReadPropertyMultipleAck>> {
    private final BacnetApplicationService bacnetApplicationService;

    @Override
    public List<ReadPropertyMultipleAck> handle(BacnetReadPropertyMultipleQuery query) {
        try {
            return bacnetApplicationService.handle(query);
        } catch (Exception e) {
            log.error("", e);
        }
        return Collections.emptyList();
    }
}
