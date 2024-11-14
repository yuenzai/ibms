package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
import cn.ecosync.ibms.bacnet.service.BacnetReadPropertyMultiple;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequest;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class BacnetReadPropertyMultipleBatchQuery implements Query<List<ReadPropertyMultipleAck>> {
    private final List<BacnetReadPropertyMultiple> services;

    public BacnetReadPropertyMultipleBatchQuery(List<BacnetReadPropertyMultiple> services) {
        this.services = services;
    }

    @Override
    public HttpRequest httpRequest() {
        return HttpRequest.postMethod()
                .hostEnvironmentKey(BacnetConstants.ENV_BACNET_SERVICE_HOST)
                .pathSegments(BacnetConstants.BACNET, "service", "read-property-multiple", "batch")
                .requestBody(services)
                .build();
    }
}
