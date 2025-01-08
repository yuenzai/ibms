package cn.ecosync.ibms.bacnet.command;

import cn.ecosync.ibms.bacnet.model.BacnetDevice;
import cn.ecosync.ibms.device.command.AddDeviceCommand;
import cn.ecosync.iframework.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString(callSuper = true)
public class AddBacnetDeviceCommand extends AddDeviceCommand {
    @Valid
    private List<BacnetDeviceProperties> devices;

    @Override
    public List<BacnetDevice> toDevices() {
        return CollectionUtils.nullSafeOf(devices).stream()
                .map(this::toBacnetDeviceDto)
                .collect(Collectors.toList());
    }

    private BacnetDevice toBacnetDeviceDto(BacnetDeviceProperties device) {
        return new BacnetDevice(device.getDeviceCode(), this.getSchemasCode(), device.getDeviceName(), device.getDeviceInstance());
    }

    @JsonIgnore
    @AssertTrue(message = "Device code duplicated")
    public boolean isUniqueDeviceCode() {
        return CollectionUtils.hasUniqueElement(CollectionUtils.nullSafeOf(devices), BacnetDeviceProperties::getDeviceCode);
    }

    @JsonIgnore
    @AssertTrue(message = "DeviceInstance duplicated")
    public boolean isUniqueDeviceInstance() {
        return CollectionUtils.hasUniqueElement(CollectionUtils.nullSafeOf(devices), BacnetDeviceProperties::getDeviceInstance);
    }

    @Getter
    @ToString
    public static class BacnetDeviceProperties {
        @NotBlank
        private String deviceCode;
        private String deviceName;
        @NotNull
        private Integer deviceInstance;
    }
}
