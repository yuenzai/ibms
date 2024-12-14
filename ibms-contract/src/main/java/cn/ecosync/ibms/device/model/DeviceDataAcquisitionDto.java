package cn.ecosync.ibms.device.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.ToString;

import java.util.Objects;

@ToString
public class DeviceDataAcquisitionDto implements DeviceDataAcquisitionModel {
    @JsonUnwrapped
    private DeviceDataAcquisitionId daqId;
    private DeviceDataAcquisitionProperties daqProperties;

    protected DeviceDataAcquisitionDto() {
    }

    public DeviceDataAcquisitionDto(DeviceDataAcquisitionId daqId, DeviceDataAcquisitionProperties daqProperties) {
        this.daqId = daqId;
        this.daqProperties = daqProperties;
    }

    @Override
    public DeviceDataAcquisitionId getDaqId() {
        return daqId;
    }

    @Override
    public DeviceDataAcquisitionProperties getDaqProperties() {
        return daqProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDataAcquisitionDto)) return false;
        DeviceDataAcquisitionDto that = (DeviceDataAcquisitionDto) o;
        return Objects.equals(daqId, that.daqId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(daqId);
    }
}
