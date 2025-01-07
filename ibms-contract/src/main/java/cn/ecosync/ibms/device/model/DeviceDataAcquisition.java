package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDataAcquisition;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = BacnetDataAcquisition.class, name = "BACNET"))
public abstract class DeviceDataAcquisition implements IDeviceDataAcquisition {
    @Valid
    @NotNull
    @JsonUnwrapped
    private DeviceDataAcquisitionId dataAcquisitionId;

    protected DeviceDataAcquisition() {
    }

    protected DeviceDataAcquisition(DeviceDataAcquisitionId dataAcquisitionId) {
        Assert.notNull(dataAcquisitionId, "dataAcquisitionId must not be null");
        this.dataAcquisitionId = dataAcquisitionId;
    }

    @Override
    public abstract DeviceSchemas getSchemas();

    @Override
    public abstract List<? extends Device> getDevices();

    public abstract DeviceDataAcquisition addDeviceReferences(Collection<Device> devices);

    public abstract DeviceDataAcquisition removeDeviceReferences(Collection<Device> devices);

    public abstract DeviceDataAcquisition withSchemas(DeviceSchemas schemas);

    public abstract DeviceDataAcquisition withDevices(Collection<Device> devices);

    public abstract DeviceDataAcquisition toReference();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceDataAcquisition)) return false;
        DeviceDataAcquisition that = (DeviceDataAcquisition) o;
        return Objects.equals(this.dataAcquisitionId, that.dataAcquisitionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dataAcquisitionId);
    }
}
