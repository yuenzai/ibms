package cn.ecosync.ibms.scheduling.domain;

import cn.ecosync.ibms.scheduling.dto.SchedulingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SchedulingReadonlyRepository {
    List<SchedulingDto> listSearch();

    Page<SchedulingDto> pageSearch(Pageable pageable);
}
