package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@Embeddable
public class DeviceDataAcquisitionId {
    @NotBlank
    @Column(name = "daq_name", nullable = false, updatable = false)
    private String daqName;

    protected DeviceDataAcquisitionId() {
    }

    public DeviceDataAcquisitionId(String daqName) {
        Assert.hasText(daqName, "daqName must not be empty");
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
}
