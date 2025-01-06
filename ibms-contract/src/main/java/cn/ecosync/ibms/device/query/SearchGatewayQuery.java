package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.iframework.query.PageQuery;
import cn.ecosync.iframework.query.Query;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(callSuper = true)
public class SearchGatewayQuery extends PageQuery implements Query<List<DeviceGateway>> {
}
