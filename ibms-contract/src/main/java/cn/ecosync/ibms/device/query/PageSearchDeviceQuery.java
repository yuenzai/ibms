package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceModel;
import cn.ecosync.iframework.query.Query;
import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

@Getter
@ToString
public class PageSearchDeviceQuery implements Query<Page<DeviceModel>> {
    @Size(min = 1)
    private String daqName;
    @NotNull
    private Integer page;
    @NotNull
    private Integer pagesize;

    protected PageSearchDeviceQuery() {
    }

    public PageSearchDeviceQuery(Integer page, Integer pagesize, String daqName) {
        Assert.notNull(page, "page must not be null");
        Assert.notNull(pagesize, "pagesize must not be null");
        if (daqName != null && daqName.isEmpty()) {
            throw new IllegalArgumentException("daqName must not be empty");
        }
        this.page = page;
        this.pagesize = pagesize;
        this.daqName = daqName;
    }

    public Pageable toPageable() {
        return CollectionUtils.of(page, pagesize);
    }

    public DeviceDataAcquisitionId toDeviceDataAcquisitionId() {
        if (daqName == null) return null;
        return new DeviceDataAcquisitionId(daqName);
    }
}
