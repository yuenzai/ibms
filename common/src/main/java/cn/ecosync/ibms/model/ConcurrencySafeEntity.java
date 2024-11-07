package cn.ecosync.ibms.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * 并发安全实体
 *
 * @author 覃俊元
 * @since 2024
 */
@MappedSuperclass
public abstract class ConcurrencySafeEntity extends Entity {
    /**
     * 乐观锁版本
     */
    @Version
    private Integer version;
}
