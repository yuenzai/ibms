package cn.ecosync.ibms.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class IdentifiedDomainObject implements Serializable {
    /**
     * 主键
     */
    protected Integer id;
}
