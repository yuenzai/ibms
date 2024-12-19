package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.*;
import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class AddBacnetDeviceCommand extends AddDeviceCommand {
    @Valid
    private List<BacnetDeviceProperties> devicesProperties;

    protected AddBacnetDeviceCommand() {
    }

    public List<BacnetDeviceProperties> getDevicesProperties() {
        return CollectionUtils.nullSafeOf(devicesProperties);
    }

    @Override
    public List<DeviceModel> newDevices() {
        List<DeviceModel> devices = new ArrayList<>(getDevicesProperties().size());
        for (BacnetDeviceProperties deviceProperties : getDevicesProperties()) {
            DeviceId deviceId = new DeviceId(getDaqId(), deviceProperties);
            DeviceDto device = new DeviceDto(deviceId, getDaqId(), deviceProperties.getDeviceProperties());
            devices.add(device);
        }
        return devices;
    }

    @Getter
    public static class BacnetDeviceProperties implements DeviceSpecificId {
        @NotNull
        private Integer deviceInstance;
        @Valid
        @JsonUnwrapped
        private DeviceProperties deviceProperties;

        protected BacnetDeviceProperties() {
        }

        @Override
        public String toStringId() {
            return "" + deviceInstance;
        }
    }
}
