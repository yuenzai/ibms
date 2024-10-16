//package cn.ecosync.ibms.bacnet.query;
//
//import cn.ecosync.ibms.bacnet.BacnetConstants;
//import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
//import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
//import cn.ecosync.ibms.query.Query;
//import cn.ecosync.ibms.util.HttpRequest;
//import lombok.Getter;
//import lombok.ToString;
//
//import java.util.List;
//
//@Getter
//@ToString
//public class BacnetReadPropertyMultipleQuery implements Query<List<ReadPropertyMultipleAck>> {
//    private final BacnetReadPropertyMultipleService readPropertyMultipleService;
//
//    public BacnetReadPropertyMultipleQuery(BacnetReadPropertyMultipleService readPropertyMultipleService) {
//        this.readPropertyMultipleService = readPropertyMultipleService;
//    }
//
//    @Override
//    public HttpRequest httpRequest() {
//        return HttpRequest.postMethod()
//                .hostEnvironmentKey(BacnetConstants.ENV_BACNET_SERVICE_HOST)
//                .pathSegments(BacnetConstants.BACNET, "readpropm")
//                .requestBody(this.readPropertyMultipleService)
//                .build();
//    }
//}
