package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequest;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@ToString
public class BacnetReadPropertyMultipleBatchQuery implements Query<List<ReadPropertyMultipleAck>> {
    @Valid
    @NotEmpty
    @JsonUnwrapped
    private List<BacnetReadPropertyMultipleService> services;

    protected BacnetReadPropertyMultipleBatchQuery() {
    }

    public BacnetReadPropertyMultipleBatchQuery(List<BacnetReadPropertyMultipleService> services) {
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
