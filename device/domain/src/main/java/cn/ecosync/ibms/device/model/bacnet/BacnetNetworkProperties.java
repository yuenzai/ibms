package cn.ecosync.ibms.device.model.bacnet;

import cn.ecosync.ibms.system.model.DictionaryValue;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetNetworkProperties implements DictionaryValue {
    public static final String TYPE = "bacnet-network";
    private String networkName;

    @Override
    public String type() {
        return TYPE;
    }
}
