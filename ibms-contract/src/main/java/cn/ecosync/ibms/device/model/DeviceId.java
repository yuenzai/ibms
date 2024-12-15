package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Matcher;

import static cn.ecosync.ibms.Constants.REGEX_CODE;

@Getter
@Embeddable
public class DeviceId {
    public static final String REGEX = "(" + DeviceDataAcquisitionId.REGEX + ")" + "\\|" + REGEX_CODE;
    public static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile(REGEX);
    public static final String ERROR_REGEX_NOT_MATCH = "deviceCode not match regex: " + REGEX;

    @Pattern(regexp = REGEX, message = ERROR_REGEX_NOT_MATCH)
    @Column(name = "device_code", nullable = false, updatable = false)
    private String deviceCode;

    protected DeviceId() {
    }

    public DeviceId(String deviceCode) {
        Assert.isTrue(PATTERN.matcher(deviceCode).matches(), ERROR_REGEX_NOT_MATCH);
        this.deviceCode = deviceCode;
    }

    public DeviceDataAcquisitionId toDaqId() {
        Matcher matcher = PATTERN.matcher(deviceCode);
        Assert.isTrue(matcher.matches(), ERROR_REGEX_NOT_MATCH);
        return new DeviceDataAcquisitionId(matcher.group(1));
    }

    @Override
    public String toString() {
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
}
