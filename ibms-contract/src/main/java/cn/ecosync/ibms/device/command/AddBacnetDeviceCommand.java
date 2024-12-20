package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.*;
import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@ToString
public class AddBacnetDeviceCommand extends AddDeviceCommand {
    @Valid
    private List<BacnetDeviceProperties> devicesProperties;
    private List<Integer> deviceInstances;

    protected AddBacnetDeviceCommand() {
    }

    public List<BacnetDeviceProperties> getDevicesProperties() {
        return CollectionUtils.nullSafeOf(devicesProperties);
    }

    public List<Integer> getDeviceInstances() {
        return CollectionUtils.nullSafeOf(deviceInstances);
    }

    @Override
    public List<DeviceModel> newDevices() {
        List<BacnetDeviceProperties> devicesProperties = getDevicesProperties();
        if (devicesProperties.isEmpty()) {
            devicesProperties = getDeviceInstances().stream()
                    .filter(Objects::nonNull)
                    .map(BacnetDeviceProperties::new)
                    .collect(Collectors.toList());
        }

        if (devicesProperties.isEmpty()) return Collections.emptyList();

        List<DeviceModel> devices = new ArrayList<>(devicesProperties.size());
        for (BacnetDeviceProperties deviceProperties : devicesProperties) {
            DeviceId deviceId = new DeviceId(getDaqId(), deviceProperties);
            DeviceDto device = new DeviceDto(deviceId, getDaqId(), deviceProperties.getDeviceProperties());
            devices.add(device);
        }
        return devices;
    }

    @JsonIgnore
    @AssertTrue(message = "deviceInstance not unique")
    public boolean isUniqueDeviceInstance() {
        if (!getDevicesProperties().isEmpty()) {
            return CollectionUtils.hasUniqueElement(getDevicesProperties(), BacnetDeviceProperties::getDeviceInstance);
        }
        return CollectionUtils.hasUniqueElement(getDeviceInstances());
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

        public BacnetDeviceProperties(Integer deviceInstance) {
            this(deviceInstance, null);
        }

        public BacnetDeviceProperties(Integer deviceInstance, DeviceProperties deviceProperties) {
            Assert.notNull(deviceInstance, "deviceInstance must not be null");
            this.deviceInstance = deviceInstance;
            this.deviceProperties = deviceProperties;
        }

        @Override
        public String toStringId() {
            return "" + deviceInstance;
        }
    }
}
