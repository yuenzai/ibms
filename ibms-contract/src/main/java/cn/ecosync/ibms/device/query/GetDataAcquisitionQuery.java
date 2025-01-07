package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.IDeviceDataAcquisition;
import cn.ecosync.iframework.query.PageQuery;
import cn.ecosync.iframework.query.Query;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class GetDataAcquisitionQuery extends PageQuery implements Query<IDeviceDataAcquisition> {
    @NotBlank
    private String dataAcquisitionCode;

    protected GetDataAcquisitionQuery() {
    }

    public GetDataAcquisitionQuery(DeviceDataAcquisitionId dataAcquisitionId) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        this.dataAcquisitionCode = dataAcquisitionId.toString();
    }
}
