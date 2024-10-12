package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.jpa.DevicePointPropertiesJpaConverter;
import cn.ecosync.ibms.util.StringUtils;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Convert;
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
    @Convert(converter = DevicePointPropertiesJpaConverter.class)
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
