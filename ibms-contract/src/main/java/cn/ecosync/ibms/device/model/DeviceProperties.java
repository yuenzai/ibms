package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Embeddable
public class DeviceProperties {
    @Column(name = "device_name", nullable = false)
    private String deviceName;
    @Column(name = "description", nullable = false)
    private String description;

    protected DeviceProperties() {
    }

    public DeviceProperties(String deviceName, String description) {
        this.deviceName = StringUtils.nullSafeOf(deviceName);
        this.description = StringUtils.nullSafeOf(description);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceProperties)) return false;
        DeviceProperties that = (DeviceProperties) o;
        return Objects.equals(deviceName, that.deviceName) && Objects.equals(description, that.description);
    }

    public static DeviceProperties newProbe(String deviceName) {
        DeviceProperties probe = new DeviceProperties();
        probe.deviceName = deviceName;
        return probe;
    }
}
