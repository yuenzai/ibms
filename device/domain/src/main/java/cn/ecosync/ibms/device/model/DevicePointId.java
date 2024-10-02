package cn.ecosync.ibms.device.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@EqualsAndHashCode
public class DevicePointId {
    @Column(name = "device_code", nullable = false, updatable = false)
    private String deviceCode;
    @Column(name = "point_code", nullable = false, updatable = false)
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
