package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.device.model.DeviceNetworkProperties;
import cn.ecosync.ibms.scheduling.model.SchedulingTrigger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
@EqualsAndHashCode
public class BacnetNetworkProperties implements DeviceNetworkProperties {
    public static final String TYPE = "bacnet-network";
    @NotBlank
    private String networkName;
    @Valid
    private SchedulingTrigger schedulingTrigger;
}
