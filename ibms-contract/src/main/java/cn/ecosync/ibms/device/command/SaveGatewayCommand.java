package cn.ecosync.ibms.device.command;

import cn.ecosync.iframework.command.Command;
import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class SaveGatewayCommand implements Command {
    @NotBlank
    private String gatewayCode;
    private List<String> dataAcquisitionCodes;

    public List<String> getDataAcquisitionCodes() {
        return CollectionUtils.nullSafeOf(dataAcquisitionCodes);
    }
}
