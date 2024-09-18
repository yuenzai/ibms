package cn.ecosync.ibms.model;

public interface AggregateRoot {
    String aggregateType();

    String aggregateId();
}
