package cn.ecosync.ibms.bacnet.command;

import cn.ecosync.ibms.bacnet.model.BacnetDevice;
import cn.ecosync.ibms.device.command.SaveDeviceCommand;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;

@ToString
public class SaveBacnetDeviceCommand implements SaveDeviceCommand {
    @Valid
    @NotNull
    @JsonUnwrapped
    private BacnetDevice device;

    @Override
    public BacnetDevice getDevice() {
        return device;
    }
}
