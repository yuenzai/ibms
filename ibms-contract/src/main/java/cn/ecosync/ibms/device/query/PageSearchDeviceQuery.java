package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.dto.DeviceDto;
import cn.ecosync.iframework.query.Query;
import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
public class PageSearchDeviceQuery implements Query<Page<DeviceDto>> {
    @NotNull
    private Integer page;
    @NotNull
    private Integer pagesize;
    private Boolean readonly;

    public Pageable toPageable() {
        return CollectionUtils.of(page, pagesize);
    }

    public Boolean getReadonly() {
        return readonly != null ? readonly : false;
    }
}
