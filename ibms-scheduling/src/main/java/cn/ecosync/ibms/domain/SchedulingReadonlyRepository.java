package cn.ecosync.ibms.domain;

import cn.ecosync.ibms.dto.SchedulingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SchedulingReadonlyRepository {
    List<SchedulingDto> listing();

    Page<SchedulingDto> paging(Pageable pageable);

    default Iterable<SchedulingDto> search(Pageable pageable) {
        return pageable.isPaged() ? paging(pageable) : listing();
    }
}
