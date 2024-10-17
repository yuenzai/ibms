package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.BacnetConstants;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DeviceStatus;
import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.util.HttpRequest;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class GetBacnetDeviceStatusBatchQuery implements Query<List<DeviceStatus>> {
    private final List<DeviceDto> deviceDtoList;

    public GetBacnetDeviceStatusBatchQuery(List<DeviceDto> deviceDtoList) {
        this.deviceDtoList = deviceDtoList;
    }

    @Override
    public HttpRequest httpRequest() {
        return HttpRequest.postMethod()
                .hostEnvironmentKey(BacnetConstants.ENV_BACNET_SERVICE_HOST)
                .pathSegments(BacnetConstants.BACNET, "readpropm", "batch")
                .requestBody(deviceDtoList)
                .build();
    }
}
