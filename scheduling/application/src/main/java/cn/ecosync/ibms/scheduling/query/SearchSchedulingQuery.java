package cn.ecosync.ibms.scheduling.query;

import cn.ecosync.ibms.query.Query;
import cn.ecosync.ibms.scheduling.dto.SchedulingStatusDto;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
@RequiredArgsConstructor
public class SearchSchedulingQuery implements Query<Iterable<SchedulingStatusDto>> {
    private final Integer page;
    private final Integer pageSize;

    public Pageable toPageable() {
        return CollectionUtils.of(page, pageSize);
    }
}
