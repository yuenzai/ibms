package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequest;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.validation.Valid;

@Getter
@ToString
public class BacnetReadPropertyMultipleQuery implements Query<ReadPropertyMultipleAck> {
    @Valid
    @JsonUnwrapped
    private BacnetReadPropertyMultipleService service;

    protected BacnetReadPropertyMultipleQuery() {
    }

    public BacnetReadPropertyMultipleQuery(BacnetReadPropertyMultipleService service) {
        Assert.notNull(service, "BacnetReadPropertyMultiple must not be null");
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
