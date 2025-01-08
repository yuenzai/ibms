package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@Embeddable
public class DeviceId {
    @Column(name = "device_code", nullable = false, updatable = false)
    private String deviceCode;

    protected DeviceId() {
    }

    public DeviceId(String deviceCode) {
        Assert.hasText(deviceCode, "deviceCode must not be null");
        this.deviceCode = deviceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceId)) return false;
        DeviceId that = (DeviceId) o;
        return Objects.equals(this.deviceCode, that.deviceCode);
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
