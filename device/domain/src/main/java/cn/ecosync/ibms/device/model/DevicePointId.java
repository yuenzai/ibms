package cn.ecosync.ibms.device.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
@EqualsAndHashCode
public class DevicePointId {
    private String pointCode;

    protected DevicePointId() {
    }

    public DevicePointId(String pointCode) {
        Assert.hasText(pointCode, "point code must not be empty");
        this.pointCode = pointCode;
    }

    @Override
    public String toString() {
        return pointCode;
    }
}
