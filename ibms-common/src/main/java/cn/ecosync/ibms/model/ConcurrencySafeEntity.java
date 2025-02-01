package cn.ecosync.ibms.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * @author yuenzai
 * @since 2024
 */
@MappedSuperclass
public abstract class ConcurrencySafeEntity extends BaseEntity {
    /**
     * 乐观锁版本
     */
    @Version
    protected Integer version;
}
