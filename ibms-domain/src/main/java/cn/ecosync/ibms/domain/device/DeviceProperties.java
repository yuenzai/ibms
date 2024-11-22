package cn.ecosync.ibms.domain.device;

import cn.ecosync.ibms.device.dto.DeviceExtra;
import cn.ecosync.iframework.util.StringUtils;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Embeddable
public class DeviceProperties {
    @Column(name = "device_name", nullable = false)
    private String deviceName = "";

    @Column(name = "path", nullable = false)
    private String path = "";

    @Column(name = "description", nullable = false)
    private String description = "";

    @Valid
    @NotNull
    @Column(name = "device_extra", nullable = false)
    private DeviceExtra deviceExtra;

    protected DeviceProperties() {
    }

    public DeviceProperties(String deviceName, String path, String description, DeviceExtra deviceExtra) {
        Assert.notNull(deviceExtra, "deviceExtra can not be null");
        this.deviceName = StringUtils.nullSafeOf(deviceName);
        this.path = StringUtils.nullSafeOf(path);
        this.description = StringUtils.nullSafeOf(description);
        this.deviceExtra = deviceExtra;
    }
}
