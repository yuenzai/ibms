package cn.ecosync.ibms.device.command;

import cn.ecosync.iframework.command.Command;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RemoveGatewayCommand implements Command {
    @NotBlank
    private String gatewayCode;
}
