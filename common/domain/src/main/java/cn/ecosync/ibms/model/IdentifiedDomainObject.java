package cn.ecosync.ibms.model;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class IdentifiedDomainObject implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    protected Integer id;
}
