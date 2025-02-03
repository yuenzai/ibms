package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.query.PageQuery;
import cn.ecosync.ibms.query.Query;
import org.springframework.data.domain.Page;

public class SearchDataAcquisitionQuery extends PageQuery implements Query<Page<DeviceDataAcquisition>> {
    public SearchDataAcquisitionQuery(Integer page, Integer pagesize) {
        super(page, pagesize);
    }
}
