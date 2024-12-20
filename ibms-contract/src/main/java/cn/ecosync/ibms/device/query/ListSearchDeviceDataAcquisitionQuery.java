package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.iframework.query.Query;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@ToString
public class ListSearchDeviceDataAcquisitionQuery implements Query<List<DeviceDataAcquisitionModel>> {
    public Sort toSort() {
        return Sort.unsorted();
    }
}
