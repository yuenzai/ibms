package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@Embeddable
public class DeviceId {
    @NotBlank
    @Column(name = "daq_name", nullable = false, updatable = false)
    private String daqName;
    @NotBlank
    @Column(name = "device_code", nullable = false, updatable = false)
    private String deviceCode;

    protected DeviceId() {
    }

    public DeviceId(String daqName, String deviceCode) {
        Assert.hasText(daqName, "daqName must not be empty");
        Assert.hasText(deviceCode, "deviceCode must not be empty");
        this.daqName = daqName;
        this.deviceCode = deviceCode;
    }

    @Override
    public String toString() {
        return daqName + "|" + deviceCode;
    }

    public static DeviceId fromString(String str) {
        String[] idStr = str.split("\\|");
        String daqName = idStr[0];
        String deviceCode = idStr[1];
        return new DeviceId(daqName, deviceCode);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceId)) return false;
        DeviceId deviceId = (DeviceId) o;
        return Objects.equals(daqName, deviceId.daqName) && Objects.equals(deviceCode, deviceId.deviceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(daqName, deviceCode);
    }

    public static DeviceId newProbe(String daqName, String deviceCode) {
        DeviceId probe = new DeviceId();
        probe.daqName = daqName;
        probe.deviceCode = deviceCode;
        return probe;
    }
}
