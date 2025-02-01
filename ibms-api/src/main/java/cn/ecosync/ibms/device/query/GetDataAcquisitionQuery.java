package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.IDeviceDataAcquisition;
import cn.ecosync.ibms.query.Query;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetDataAcquisitionQuery implements Query<IDeviceDataAcquisition> {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;
}
