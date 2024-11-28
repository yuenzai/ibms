package cn.ecosync.ibms.scheduling.query;

import cn.ecosync.ibms.scheduling.dto.SchedulingDto;
import cn.ecosync.iframework.query.Query;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ListSearchSchedulingQuery implements Query<List<SchedulingDto>> {
    private Integer maxCount;

    public Integer getMaxCount() {
        return maxCount != null ? maxCount : 5;
    }
}
