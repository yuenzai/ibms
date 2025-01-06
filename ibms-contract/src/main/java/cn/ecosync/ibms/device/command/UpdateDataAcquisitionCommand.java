package cn.ecosync.ibms.device.command;

import cn.ecosync.iframework.CollectionOperationEnum;
import cn.ecosync.iframework.command.Command;
import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Getter
@ToString
public class UpdateDataAcquisitionCommand implements Command {
    @NotBlank
    private String dataAcquisitionCode;
    @Valid
    private Devices devices;

    public Optional<Devices> getDevices() {
        return Optional.ofNullable(devices);
    }

    @Getter
    @ToString
    public static class Devices {
        private List<String> deviceCodes;
        @NotNull
        private CollectionOperationEnum operation;

        public List<String> getDeviceCodes() {
            return CollectionUtils.nullSafeOf(deviceCodes);
        }
    }
}
