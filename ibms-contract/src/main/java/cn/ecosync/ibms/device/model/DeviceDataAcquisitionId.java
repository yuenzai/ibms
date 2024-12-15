package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

import static cn.ecosync.ibms.Constants.REGEX_CODE;

@Getter
@Embeddable
public class DeviceDataAcquisitionId {
    public static final String REGEX = REGEX_CODE + "\\|" + REGEX_CODE;
    public static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile(REGEX);
    public static final String ERROR_REGEX_NOT_MATCH = "daqName not match regex: " + REGEX;

    @Pattern(regexp = REGEX, message = ERROR_REGEX_NOT_MATCH)
    @Column(name = "daq_name", nullable = false, updatable = false)
    private String daqName;

    protected DeviceDataAcquisitionId() {
    }

    public DeviceDataAcquisitionId(String daqName) {
        Assert.isTrue(PATTERN.matcher(daqName).matches(), ERROR_REGEX_NOT_MATCH);
        this.daqName = daqName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDataAcquisitionId)) return false;
        DeviceDataAcquisitionId that = (DeviceDataAcquisitionId) o;
        return Objects.equals(daqName, that.daqName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(daqName);
    }

    @Override
    public String toString() {
        return daqName;
    }

    public static DeviceDataAcquisitionId newProbe(String daqName) {
        DeviceDataAcquisitionId probe = new DeviceDataAcquisitionId();
        probe.daqName = daqName;
        return probe;
    }
}
