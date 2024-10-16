package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DeviceStatus;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequest;
import lombok.Getter;

@Getter
public class GetBacnetDeviceStatusQuery implements Query<DeviceStatus> {
    private final DeviceDto deviceDto;

    public GetBacnetDeviceStatusQuery(DeviceDto deviceDto) {
        this.deviceDto = deviceDto;
    }

    @Override
    public HttpRequest httpRequest() {
        return HttpRequest.postMethod()
                .hostEnvironmentKey(BacnetConstants.ENV_BACNET_SERVICE_HOST)
                .pathSegments(BacnetConstants.BACNET, "readpropm")
                .requestBody(deviceDto)
                .build();
    }
}
