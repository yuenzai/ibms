package cn.ecosync.ibms.model;

/**
 * 并发安全实体
 *
 * @author 覃俊元
 * @since 2024
 */
public abstract class ConcurrencySafeEntity extends Entity {
    /**
     * 乐观锁版本
     */
    private Integer version;
}
