package cn.ecosync.ibms.device.domain;

import cn.ecosync.ibms.device.dto.DevicePointExtra;
import cn.ecosync.iframework.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
@Embeddable
public class DevicePointProperties {
    @Column(name = "point_name", nullable = false)
    private String pointName = "";

    @Valid
    @NotNull
    @Column(name = "point_extra", nullable = false)
    private DevicePointExtra pointExtra;

    protected DevicePointProperties() {
    }

    public DevicePointProperties(String pointName, DevicePointExtra pointExtra) {
        Assert.notNull(pointExtra, "pointExtra can not be null");
        this.pointName = StringUtils.nullSafeOf(pointName);
        this.pointExtra = pointExtra;
    }
}
