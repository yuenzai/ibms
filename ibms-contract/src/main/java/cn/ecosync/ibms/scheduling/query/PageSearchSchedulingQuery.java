package cn.ecosync.ibms.scheduling.query;

import cn.ecosync.ibms.scheduling.model.SchedulingQueryModel;
import cn.ecosync.iframework.query.Query;
import cn.ecosync.iframework.util.CollectionUtils;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
public class PageSearchSchedulingQuery implements Query<Page<SchedulingQueryModel>> {
    @NotNull
    private Integer page;
    @NotNull
    private Integer pageSize;

    public Pageable toPageable() {
        return CollectionUtils.of(page, pageSize);
    }
}
