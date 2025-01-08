package cn.ecosync.ibms.device.command;

import cn.ecosync.ibms.device.model.IDeviceGateway.SynchronizationStateEnum;
import cn.ecosync.iframework.command.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class SetGatewaySynchronizationStateCommand implements Command {
    @NotBlank
    private String gatewayCode;
    @Setter
    @JsonIgnore
    private SynchronizationStateEnum synchronizationState;

    protected SetGatewaySynchronizationStateCommand() {
    }

    public SetGatewaySynchronizationStateCommand(String gatewayCode, SynchronizationStateEnum synchronizationState) {
        this.gatewayCode = gatewayCode;
        this.synchronizationState = synchronizationState;
    }

}
