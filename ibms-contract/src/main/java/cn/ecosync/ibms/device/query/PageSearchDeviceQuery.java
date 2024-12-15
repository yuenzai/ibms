package cn.ecosync.ibms.device.query;

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
    @NotNull
    private Integer page;
    @NotNull
    private Integer pagesize;
    @Size(min = 1)
    private String daqName;
    @Size(min = 1)
    private String deviceCode;
    @Size(min = 1)
    private String deviceName;
    @Size(min = 1)
    private String path;

    protected PageSearchDeviceQuery() {
    }

    public PageSearchDeviceQuery(Integer page, Integer pagesize, String daqName, String deviceCode, String deviceName, String path) {
        Assert.notNull(page, "page must not be null");
        Assert.notNull(pagesize, "pagesize must not be null");
        Assert.isTrue(daqName != null && daqName.isEmpty(), "daqName must not be empty");
        Assert.isTrue(deviceCode != null && deviceCode.isEmpty(), "deviceCode must not be empty");
        Assert.isTrue(deviceName != null && deviceName.isEmpty(), "deviceName must not be empty");
        Assert.isTrue(path != null && path.isEmpty(), "path must not be empty");
        this.page = page;
        this.pagesize = pagesize;
        this.daqName = daqName;
        this.deviceCode = deviceCode;
        this.deviceName = deviceName;
        this.path = path;
    }

    public Pageable toPageable() {
        return CollectionUtils.of(page, pagesize);
    }
}
