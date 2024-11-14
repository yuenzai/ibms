package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
import cn.ecosync.ibms.bacnet.service.BacnetReadPropertyMultiple;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequest;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetReadPropertyMultipleQuery implements Query<ReadPropertyMultipleAck> {
    private final BacnetReadPropertyMultiple service;

    public BacnetReadPropertyMultipleQuery(BacnetReadPropertyMultiple service) {
        this.service = service;
    }

    @Override
    public HttpRequest httpRequest() {
        return HttpRequest.postMethod()
                .hostEnvironmentKey(BacnetConstants.ENV_BACNET_SERVICE_HOST)
                .pathSegments(BacnetConstants.BACNET, "service", "read-property-multiple")
                .requestBody(service)
                .build();
    }
}
