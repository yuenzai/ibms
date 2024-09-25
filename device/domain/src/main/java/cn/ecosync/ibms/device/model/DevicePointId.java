package cn.ecosync.ibms.device.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
@EqualsAndHashCode
public class DevicePointId {
    private String deviceCode;
    private String pointCode;

    protected DevicePointId() {
    }

    public DevicePointId(String deviceCode, String pointCode) {
        Assert.hasText(deviceCode, "device code must not be empty");
        Assert.hasText(pointCode, "point code must not be empty");
        this.deviceCode = deviceCode;
        this.pointCode = pointCode;
    }

    @Override
    public String toString() {
        return deviceCode + "-" + pointCode;
    }
}
