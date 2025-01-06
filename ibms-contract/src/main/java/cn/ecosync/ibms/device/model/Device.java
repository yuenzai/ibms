package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDevice;
import cn.ecosync.iframework.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes(@JsonSubTypes.Type(value = BacnetDevice.class, name = "BACNET"))
public abstract class Device implements IDevice {
    @Valid
    @NotNull
    @JsonUnwrapped
    private DeviceId deviceId;
    @Valid
    @NotNull
    @JsonUnwrapped
    protected DeviceSchemasId schemasId;
    @Setter
    private String deviceName;

    protected Device() {
    }

    protected Device(DeviceId deviceId) {
        Assert.notNull(deviceId, "deviceId must not be null");
        this.deviceId = deviceId;
    }

    public Device(DeviceId deviceId, DeviceSchemasId schemasId) {
        Assert.notNull(deviceId, "deviceId must not be null");
        Assert.notNull(schemasId, "schemasId must not be null");
        this.deviceId = deviceId;
        this.schemasId = schemasId;
    }

    public String getDeviceName() {
        return StringUtils.nullSafeOf(deviceName);
    }

    public abstract Device toReference();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Device)) return false;
        Device other = (Device) o;
        return Objects.equals(this.deviceId, other.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceId);
    }
}
