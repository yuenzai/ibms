package cn.ecosync.ibms.query;

import cn.ecosync.ibms.dto.DeviceDto;
import cn.ecosync.iframework.query.Query;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class SearchDeviceListQuery implements Query<List<DeviceDto>> {
    private final Boolean readonly;

    public SearchDeviceListQuery(Boolean readonly) {
        this.readonly = readonly;
    }
}
