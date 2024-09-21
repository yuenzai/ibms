package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.system.model.DictionaryValue;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class BacnetNetworkProperties implements DictionaryValue {
    public static final String TYPE = "bacnet-network";
    @NotBlank
    private String networkName;
}
