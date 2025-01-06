package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.iframework.query.Query;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class GetGatewayQuery implements Query<DeviceGateway> {
    @NotBlank
    private String gatewayCode;

    protected GetGatewayQuery() {
    }

    public GetGatewayQuery(String gatewayCode) {
        Assert.hasText(gatewayCode, "gatewayCode must not be null");
        this.gatewayCode = gatewayCode;
    }
}
