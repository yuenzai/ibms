package cn.ecosync.ibms.gateway.query;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.query.PageQuery;
import cn.ecosync.ibms.query.Query;
import org.springframework.data.domain.Page;

public class SearchDataAcquisitionQuery extends PageQuery implements Query<Page<DeviceDataAcquisition>> {
    public SearchDataAcquisitionQuery() {
        super();
    }

    public SearchDataAcquisitionQuery(Integer page, Integer pagesize) {
        super(page, pagesize);
    }
}
