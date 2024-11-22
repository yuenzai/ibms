package cn.ecosync.ibms.command;

import cn.ecosync.iframework.command.Command;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@ToString
public class RemoveDevicePointCommand implements Command {
    @NotBlank
    private String deviceCode;

    private List<String> pointCodes;
}
