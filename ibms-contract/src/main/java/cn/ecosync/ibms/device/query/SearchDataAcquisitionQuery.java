package cn.ecosync.ibms.device.query;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.iframework.query.PageQuery;
import cn.ecosync.iframework.query.Query;
import lombok.ToString;

import java.util.List;

@ToString
public class SearchDataAcquisitionQuery extends PageQuery implements Query<List<DeviceDataAcquisition>> {
}
