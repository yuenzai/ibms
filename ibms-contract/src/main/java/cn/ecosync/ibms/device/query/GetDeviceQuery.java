package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.iframework.query.Query;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetDeviceQuery implements Query<DeviceDto> {
    @NotEmpty
    private String deviceCode;
    private Boolean readonly;

    protected GetDeviceQuery() {
    }

    public GetDeviceQuery(String deviceCode, Boolean readonly) {
        this.deviceCode = deviceCode;
        this.readonly = readonly;
    }

    public Boolean getReadonly() {
        return readonly != null ? readonly : false;
    }
}
