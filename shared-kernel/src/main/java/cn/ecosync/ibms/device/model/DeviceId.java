package cn.ecosync.ibms.device.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
@EqualsAndHashCode
public class DeviceId {
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
