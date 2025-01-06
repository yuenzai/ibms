package cn.ecosync.ibms.device.command;

import cn.ecosync.iframework.command.Command;
import cn.ecosync.iframework.util.CollectionUtils;
import lombok.ToString;

import java.util.List;

@ToString
public class RemoveDeviceCommand implements Command {
    private List<String> deviceCodes;

    public List<String> getDeviceCodes() {
        return CollectionUtils.nullSafeOf(deviceCodes);
    }

//    @JsonIgnore
//    @AssertTrue(message = "Device code duplicated")
//    public boolean isUniqueDeviceCode() {
//        return CollectionUtils.hasUniqueElement(getDeviceCodes());
//    }
}
