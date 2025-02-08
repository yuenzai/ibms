package cn.ecosync.ibms.jpa;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author yuenzai
 * @since 2024
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class JpaEntity<ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private ID id;
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Long createdDate;
    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private Long lastModifiedDate;
    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    protected ID id() {
        return id;
    }
}
