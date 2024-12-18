package cn.ecosync.ibms.device.model;

import cn.ecosync.iframework.util.ToStringId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

import java.util.Objects;

import static cn.ecosync.ibms.Constants.PATH_MATCHER;

@Embeddable
public class DeviceDataAcquisitionId implements ToStringId {
    public static final String KEY_DAQ = "daq";
    protected static final String PATTERN = "{daq}";

    @Column(name = "daq_code", nullable = false, updatable = false)
    private String daqCode;

    protected DeviceDataAcquisitionId() {
    }

    public DeviceDataAcquisitionId(String daqName) {
        Assert.hasText(daqName, "daqName must not be empty");
        String daqCode = daqName;
        Assert.isTrue(PATH_MATCHER.match(PATTERN, daqCode), "daqCode must match pattern: " + PATTERN);
        this.daqCode = daqCode;
    }

    protected String getDaqCode() {
        return daqCode;
    }

    @Override
    public String toString() {
        return toStringId();
    }

    @Override
    public String toStringId() {
        return daqCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDataAcquisitionId)) return false;
        DeviceDataAcquisitionId that = (DeviceDataAcquisitionId) o;
        return Objects.equals(daqCode, that.daqCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(daqCode);
    }

    public static DeviceDataAcquisitionId newProbe(String daqCode) {
        DeviceDataAcquisitionId probe = new DeviceDataAcquisitionId();
        probe.daqCode = daqCode;
        return probe;
    }
}
