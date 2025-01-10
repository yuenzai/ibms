package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.bacnet.model.BacnetDevice;
import cn.ecosync.iframework.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

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
    private DeviceSchemasId schemasId;
    private String deviceName;

    protected Device() {
    }

    protected Device(DeviceId deviceId) {
        Assert.notNull(deviceId, "deviceId must not be null");
        this.deviceId = deviceId;
    }

    public Device(DeviceId deviceId, DeviceSchemasId schemasId, String deviceName) {
        Assert.notNull(deviceId, "deviceId must not be null");
        Assert.notNull(schemasId, "schemasId must not be null");
        this.deviceId = deviceId;
        this.schemasId = schemasId;
        this.deviceName = StringUtils.nullSafeOf(deviceName);
    }

    public String getDeviceName() {
        return StringUtils.nullSafeOf(deviceName);
    }

    public abstract Device toReference();
}
