package cn.ecosync.ibms.scheduling.repository;

import cn.ecosync.ibms.scheduling.model.SchedulingModel;
import cn.ecosync.ibms.scheduling.model.SchedulingQueryModel;

public interface SchedulingQueryModelExtension {
    SchedulingQueryModel toQueryModel(SchedulingModel schedulingModel);
}
