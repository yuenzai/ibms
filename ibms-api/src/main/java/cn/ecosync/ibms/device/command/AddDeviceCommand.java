package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.command.Command;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@Getter
@ToString
public abstract class AddDeviceCommand implements Command {
    @NotBlank
    private String schemasCode;

    public abstract Collection<? extends Device> toDevices();
}
