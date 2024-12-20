package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.iframework.query.Query;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.util.List;

@Getter
@ToString
public class ListSearchDeviceQuery implements Query<List<DeviceModel>> {
    @Size(min = 1)
    private String daqName;
    @Size(min = 1)
    private String deviceCode;
    @Size(min = 1)
    private String deviceName;

    protected ListSearchDeviceQuery() {
    }

    public ListSearchDeviceQuery(String daqName, String deviceCode, String deviceName, String path) {
        Assert.isTrue(daqName != null && daqName.isEmpty(), "daqName must not be empty");
        Assert.isTrue(deviceCode != null && deviceCode.isEmpty(), "deviceCode must not be empty");
        Assert.isTrue(deviceName != null && deviceName.isEmpty(), "deviceName must not be empty");
        this.daqName = daqName;
        this.deviceCode = deviceCode;
        this.deviceName = deviceName;
    }

    public Sort toSort() {
        return Sort.unsorted();
    }
}
