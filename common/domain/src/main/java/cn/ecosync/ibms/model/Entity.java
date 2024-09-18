package cn.ecosync.ibms.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 实体
 *
 * @author 覃俊元
 * @since 2024
 */
public abstract class Entity extends IdentifiedDomainObject {
    /**
     * 创建时间
     */
    @CreatedDate
    private Long createdDate;
    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    private Long lastModifiedDate;
}
