package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.iframework.query.PageQuery;
import cn.ecosync.iframework.query.Query;
import lombok.ToString;
import org.springframework.data.domain.Page;

@ToString
public class SearchSchemasQuery extends PageQuery implements Query<Page<DeviceSchemas>> {
}
