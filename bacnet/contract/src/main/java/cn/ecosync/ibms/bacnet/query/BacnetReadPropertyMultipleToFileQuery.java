package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequest;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetReadPropertyMultipleToFileQuery implements Query<Void> {
    private final BacnetReadPropertyMultipleService readPropertyMultipleService;

    public BacnetReadPropertyMultipleToFileQuery(BacnetReadPropertyMultipleService readPropertyMultipleService) {
        this.readPropertyMultipleService = readPropertyMultipleService;
    }

    @Override
    public HttpRequest httpRequest() {
        return HttpRequest.postMethod()
                .hostEnvironmentKey(BacnetConstants.ENV_BACNET_SERVICE_HOST)
                .pathSegments(BacnetConstants.BACNET, "readpropmToFile")
                .requestBody(this.readPropertyMultipleService)
                .build();
    }
}
