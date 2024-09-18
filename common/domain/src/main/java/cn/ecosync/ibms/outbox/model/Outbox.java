package cn.ecosync.ibms.outbox.model;

import cn.ecosync.ibms.event.AggregateRemovedEvent;
import cn.ecosync.ibms.event.Event;
import cn.ecosync.ibms.model.IdentifiedValueObject;

public class Outbox extends IdentifiedValueObject {
    /**
     * 聚合类型
     */
    private String aggregateType;
    /**
     * 聚合唯一标识
     */
    private String aggregateId;
    /**
     * 事件唯一标识（不要加唯一约束，因为这张表不需要查询，消费者自己负责）
     */
    private String eventId;
    /**
     * 事件时间
     */
    private Long eventTime;
    /**
     * 事件类型
     */
    private String eventType;
    /**
     * 事件内容
     */
    private Object payload;

    protected Outbox() {
    }

    public Outbox(Event event) {
        this.aggregateType = event.aggregateType();
        this.aggregateId = event.aggregateId();
        this.eventId = event.eventId();
        this.eventTime = event.eventTime().toEpochMilli();
        this.eventType = event.eventType();
        this.payload = event instanceof AggregateRemovedEvent ? null : event;
    }
}
