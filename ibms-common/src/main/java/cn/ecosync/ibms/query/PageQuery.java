package cn.ecosync.ibms.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class PageQuery {
    private Integer page;
    private Integer pageSize;

    protected PageQuery() {
    }

    public PageQuery(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public Sort toSort() {
        return Sort.unsorted();
    }

    public Pageable toPageable() {
        if (page != null && pageSize != null) {
            return PageRequest.of(page, pageSize, toSort());
        } else {
            return Pageable.unpaged(toSort());
        }
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
