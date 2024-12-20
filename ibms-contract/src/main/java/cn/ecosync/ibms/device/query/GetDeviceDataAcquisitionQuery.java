package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.iframework.query.Query;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetDeviceDataAcquisitionQuery implements Query<DeviceDataAcquisitionModel> {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId daqId;
}
