package cn.ecosync.ibms.device.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
@Embeddable
@EqualsAndHashCode
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
    public String toString() {
        return deviceCode;
    }
}
