package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.query.Query;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.ToString;

@ToString
public class GetDataAcquisitionQuery implements Query<DeviceDataAcquisition> {
    @Valid
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;

    protected GetDataAcquisitionQuery() {
    }

    public GetDataAcquisitionQuery(DeviceDataAcquisitionId dataAcquisitionId) {
        this.dataAcquisitionId = dataAcquisitionId;
    }

    public DeviceDataAcquisitionId getDataAcquisitionId() {
        return dataAcquisitionId;
    }
}
