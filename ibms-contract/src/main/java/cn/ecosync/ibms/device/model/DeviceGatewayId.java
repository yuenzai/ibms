package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@Embeddable
public class DeviceGatewayId {
    @Column(name = "gateway_code", nullable = false, updatable = false)
    private String gatewayCode;

    protected DeviceGatewayId() {
    }

    public DeviceGatewayId(String gatewayCode) {
        Assert.hasText(gatewayCode, "gatewayCode must not be null");
        this.gatewayCode = gatewayCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceGatewayId)) return false;
        DeviceGatewayId that = (DeviceGatewayId) o;
        return Objects.equals(this.gatewayCode, that.gatewayCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gatewayCode);
    }

    @Override
    public String toString() {
        return gatewayCode;
    }
}
