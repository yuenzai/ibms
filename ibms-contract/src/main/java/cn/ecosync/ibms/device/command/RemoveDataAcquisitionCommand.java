package cn.ecosync.ibms.device.command;

import cn.ecosync.iframework.command.Command;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RemoveDataAcquisitionCommand implements Command {
    @NotBlank
    private String dataAcquisitionCode;
}
