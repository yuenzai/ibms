package cn.ecosync.ibms.gateway.query;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;
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
