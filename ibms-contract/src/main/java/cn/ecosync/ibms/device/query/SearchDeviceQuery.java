package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.Device;
import cn.ecosync.ibms.device.dto.DeviceProbe;
import cn.ecosync.iframework.query.PageQuery;
import cn.ecosync.iframework.query.Query;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class SearchDeviceQuery extends PageQuery implements Query<List<Device>> {
    @Valid
    @JsonUnwrapped
    private DeviceProbe probe;
}
