package cn.ecosync.ibms.scheduling.query;

import cn.ecosync.ibms.scheduling.dto.SchedulingDto;
import cn.ecosync.iframework.query.Query;
import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
public class PageSearchSchedulingQuery implements Query<Page<SchedulingDto>> {
    @NotNull
    private Integer page;
    @NotNull
    private Integer pageSize;
    private Integer maxCount;

    public Pageable toPageable() {
        return CollectionUtils.of(page, pageSize);
    }

    public Integer getMaxCount() {
        return maxCount != null ? maxCount : 5;
    }
}
