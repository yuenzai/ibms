package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.bacnet.ack.ReadPropertyMultipleAck;
import cn.ecosync.ibms.device.query.BacnetReadPropertyMultipleQuery;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BacnetReadPropertyMultipleQueryHandler implements QueryHandler<BacnetReadPropertyMultipleQuery, List<ReadPropertyMultipleAck>> {
    @Override
    public List<ReadPropertyMultipleAck> handle(BacnetReadPropertyMultipleQuery query) {
        return Collections.emptyList();
    }
}
