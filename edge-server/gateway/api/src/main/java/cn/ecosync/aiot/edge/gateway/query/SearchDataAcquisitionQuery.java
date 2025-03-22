package cn.ecosync.aiot.edge.gateway.query;

import cn.ecosync.aiot.edge.gateway.PageQuery;
import cn.ecosync.aiot.edge.gateway.model.DeviceDataAcquisition;
import cn.ecosync.aiot.query.Query;
import org.springframework.data.domain.Page;

public class SearchDataAcquisitionQuery extends PageQuery implements Query<Page<DeviceDataAcquisition>> {
    public SearchDataAcquisitionQuery() {
        super();
    }

    public SearchDataAcquisitionQuery(Integer page, Integer pagesize) {
        super(page, pagesize);
    }
}
