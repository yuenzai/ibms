package cn.ecosync.ibms.scheduling.query;

import cn.ecosync.ibms.scheduling.model.SchedulingQueryModel;
import cn.ecosync.iframework.query.Query;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@ToString
public class ListSearchSchedulingQuery implements Query<List<SchedulingQueryModel>> {
    public Sort toSort() {
        return Sort.unsorted();
    }
}
