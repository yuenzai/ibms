package cn.ecosync.ibms.device.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Getter
@EqualsAndHashCode
public class DevicePointId {
    @NotBlank
    @Column(name = "point_code", nullable = false, updatable = false)
    private String pointCode;

    protected DevicePointId() {
    }

    public DevicePointId(String pointCode) {
        Assert.hasText(pointCode, "pointCode must not be empty");
        this.pointCode = pointCode;
    }

    @Override
    public String toString() {
        return pointCode;
    }
}
