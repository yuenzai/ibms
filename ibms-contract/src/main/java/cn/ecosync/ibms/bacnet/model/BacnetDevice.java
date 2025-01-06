package cn.ecosync.ibms.bacnet.model;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString(callSuper = true)
public class BacnetDevice extends Device {
    @NotNull
    private Integer deviceInstance;

    protected BacnetDevice() {
    }

    private BacnetDevice(DeviceId deviceId) {
        super(deviceId);
    }

    public BacnetDevice(String deviceCode, String schemasCode, Integer deviceInstance) {
        this(new DeviceId(deviceCode), new DeviceSchemasId(schemasCode), deviceInstance);
    }

    public BacnetDevice(DeviceId deviceId, DeviceSchemasId schemasId, Integer deviceInstance) {
        super(deviceId, schemasId);
        Assert.notNull(deviceInstance, "deviceInstance must not be null");
        this.deviceInstance = deviceInstance;
    }

    @Override
    public BacnetDevice toReference() {
        return new BacnetDevice(getDeviceId());
    }
}
