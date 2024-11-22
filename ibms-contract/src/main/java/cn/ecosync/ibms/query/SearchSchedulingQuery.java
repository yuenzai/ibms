package cn.ecosync.ibms.query;

import cn.ecosync.ibms.dto.SchedulingDto;
import cn.ecosync.iframework.query.Query;
import cn.ecosync.iframework.util.CollectionUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
@RequiredArgsConstructor
public class SearchSchedulingQuery implements Query<Iterable<SchedulingDto>> {
    private final Integer page;
    private final Integer pageSize;
    private final Integer maxCount;

    public Pageable toPageable() {
        return CollectionUtils.of(page, pageSize);
    }

    public Integer getMaxCount() {
        return maxCount != null ? maxCount : 5;
    }
}
