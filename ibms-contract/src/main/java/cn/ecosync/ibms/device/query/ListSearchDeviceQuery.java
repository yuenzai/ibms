package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.iframework.query.Query;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ListSearchDeviceQuery implements Query<List<DeviceDto>> {
    private Boolean readonly;

    protected ListSearchDeviceQuery() {
    }

    public ListSearchDeviceQuery(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getReadonly() {
        return readonly != null ? readonly : false;
    }
}
