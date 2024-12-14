package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.iframework.query.Query;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@ToString
public class ListSearchDeviceQuery implements Query<List<DeviceModel>> {
    @Size(min = 1)
    private String daqName;

    protected ListSearchDeviceQuery() {
    }

    public ListSearchDeviceQuery(String daqName) {
        if (daqName != null && daqName.isEmpty()) {
            throw new IllegalArgumentException("daqName must not be empty");
        }
        this.daqName = daqName;
    }

    public DeviceDataAcquisitionId toDeviceDataAcquisitionId() {
        if (daqName == null) return null;
        return new DeviceDataAcquisitionId(daqName);
    }

    public Sort toSort() {
        return Sort.unsorted();
    }
}
