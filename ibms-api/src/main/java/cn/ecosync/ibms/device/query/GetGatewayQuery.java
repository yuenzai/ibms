package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum;
import cn.ecosync.ibms.query.Query;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class GetGatewayQuery implements Query<DeviceGateway> {
    @NotNull
    private DeviceGatewayId gatewayId;
    private SynchronizationStateEnum desiredState;

    protected GetGatewayQuery() {
    }

    public GetGatewayQuery(DeviceGatewayId gatewayId) {
        this(gatewayId, null);
    }

    public GetGatewayQuery(DeviceGatewayId gatewayId, SynchronizationStateEnum desiredState) {
        Assert.notNull(gatewayId, "gatewayId must not be null");
        this.gatewayId = gatewayId;
        this.desiredState = desiredState;
    }
}
