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
    @Column(name = "daq_code", nullable = false, updatable = false)
    private String dataAcquisitionCode;

    protected DeviceDataAcquisitionId() {
    }

    public DeviceDataAcquisitionId(String dataAcquisitionCode) {
        Assert.hasText(dataAcquisitionCode, "dataAcquisitionCode must not be null");
        this.dataAcquisitionCode = dataAcquisitionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDataAcquisitionId)) return false;
        DeviceDataAcquisitionId that = (DeviceDataAcquisitionId) o;
        return Objects.equals(this.dataAcquisitionCode, that.dataAcquisitionCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dataAcquisitionCode);
    }

    @Override
    public String toString() {
        return dataAcquisitionCode;
    }
}
