package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.query.PageQuery;
import cn.ecosync.ibms.query.Query;
import lombok.ToString;
import org.springframework.data.domain.Page;

@ToString
public class SearchSchemasQuery extends PageQuery implements Query<Page<DeviceSchemas>> {
}
