package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.iframework.query.PageQuery;
import cn.ecosync.iframework.query.Query;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter
@ToString(callSuper = true)
public class SearchGatewayQuery extends PageQuery implements Query<Page<DeviceGateway>> {
    public SearchGatewayQuery() {
    }

    public SearchGatewayQuery(Integer page, Integer pagesize) {
        super(page, pagesize);
    }
}
