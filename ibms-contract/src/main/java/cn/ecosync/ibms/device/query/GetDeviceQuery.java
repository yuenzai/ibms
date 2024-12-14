package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.iframework.query.Query;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class GetDeviceQuery implements Query<DeviceModel> {
    @Valid
    @JsonUnwrapped
    private DeviceId deviceId;

    protected GetDeviceQuery() {
    }

    public GetDeviceQuery(DeviceId deviceId) {
        Assert.notNull(deviceId, "deviceId must not be null");
        this.deviceId = deviceId;
    }
}
