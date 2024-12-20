package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.util.ToStringId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

import static cn.ecosync.ibms.Constants.PATH_MATCHER;

@Getter
@Embeddable
public class DeviceId implements ToStringId {
    public static final String KEY_SID = "sid";
    private static final String PATTERN = DeviceDataAcquisitionId.PATTERN + "/{sid}";

    @Column(name = "device_code", nullable = false, updatable = false)
    private String deviceCode;

    protected DeviceId() {
    }

    public DeviceId(DeviceDataAcquisitionId daqId, DeviceSpecificId sid) {
        Assert.notNull(daqId, "daqId must not be null");
        Assert.notNull(sid, "sid must not be null");
        String deviceCode = daqId.toStringId() + "/" + sid.toStringId();
        Assert.isTrue(PATH_MATCHER.match(PATTERN, deviceCode), "deviceCode must match pattern: " + PATTERN);
        this.deviceCode = deviceCode;
    }

    public DeviceId(String deviceCode) {
        Assert.isTrue(PATH_MATCHER.match(PATTERN, deviceCode), "deviceCode must match pattern: " + PATTERN);
        this.deviceCode = deviceCode;
    }

    @Override
    public String toString() {
        return toStringId();
    }

    @Override
    public String toStringId() {
        return deviceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceId)) return false;
        DeviceId deviceId = (DeviceId) o;
        return Objects.equals(deviceCode, deviceId.deviceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceCode);
    }

    public static DeviceId newProbe(String deviceCode) {
        DeviceId probe = new DeviceId();
        probe.deviceCode = deviceCode;
        return probe;
    }

    public static String extractPathVariable(String path, String key) {
        Map<String, String> variables = PATH_MATCHER.extractUriTemplateVariables(PATTERN, path);
        return variables.get(key);
    }
}
