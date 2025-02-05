package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

import static cn.ecosync.ibms.Constants.PATTERN_CODE;

@Getter
@Embeddable
public class DeviceDataPointId {
    @Column(name = "device_code", nullable = false, updatable = false)
    private String deviceCode;
    @Column(name = "metric_name", nullable = false, updatable = false)
    private String metricName;

    protected DeviceDataPointId() {
    }

    public DeviceDataPointId(String metricName, String deviceCode) {
        this.metricName = metricName;
        this.deviceCode = deviceCode;
        Assert.isTrue(validate(), "");
    }

    public String getPointName() {
        return deviceCode + "_" + metricName;
    }

    @AssertTrue
    public boolean validate() {
        return PATTERN_CODE.matcher(metricName).matches() && PATTERN_CODE.matcher(deviceCode).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDataPointId)) return false;
        DeviceDataPointId that = (DeviceDataPointId) o;
        return Objects.equals(this.metricName, that.metricName) && Objects.equals(this.deviceCode, that.deviceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metricName, deviceCode);
    }

    @Override
    public String toString() {
        return getPointName();
    }
}
