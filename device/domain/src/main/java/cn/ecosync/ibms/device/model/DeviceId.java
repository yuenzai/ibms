package cn.ecosync.ibms.device.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@EqualsAndHashCode
public class DeviceId {
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
