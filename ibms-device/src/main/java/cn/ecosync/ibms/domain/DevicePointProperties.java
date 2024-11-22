package cn.ecosync.ibms.domain;

import cn.ecosync.ibms.dto.DevicePointExtra;
import cn.ecosync.iframework.util.StringUtils;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
