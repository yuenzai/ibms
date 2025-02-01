package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.command.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AddDataAcquisitionCommand implements Command {
    @NotBlank
    private String dataAcquisitionCode;
    @NotNull
    private Long scrapeInterval;
}
