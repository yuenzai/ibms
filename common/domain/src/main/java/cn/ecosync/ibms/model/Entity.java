package cn.ecosync.ibms.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * 实体
 *
 * @author 覃俊元
 * @since 2024
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
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
