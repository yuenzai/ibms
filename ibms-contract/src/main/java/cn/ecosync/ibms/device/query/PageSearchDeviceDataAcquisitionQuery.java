package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionModel;
import cn.ecosync.iframework.query.Query;
import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
public class PageSearchDeviceDataAcquisitionQuery implements Query<Page<DeviceDataAcquisitionModel>> {
    @NotNull
    private Integer page;
    @NotNull
    private Integer pagesize;

    public Pageable toPageable() {
        return CollectionUtils.of(page, pagesize);
    }
}
