package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@Embeddable
public class DeviceId {
    @NotBlank
    @Column(name = "device_code", nullable = false, updatable = false)
    private String deviceCode;

    protected DeviceId() {
    }

    public DeviceId(String deviceCode) {
        Assert.hasText(deviceCode, "deviceCode must not be empty");
        this.deviceCode = deviceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceId)) return false;
        DeviceId deviceId = (DeviceId) o;
        return Objects.equals(deviceCode, deviceId.deviceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceCode);
    }

    @Override
    public String toString() {
        return deviceCode;
    }
}
