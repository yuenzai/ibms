package cn.ecosync.ibms.outbox.model;

import cn.ecosync.ibms.model.IdentifiedValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "outbox")
public class Outbox extends IdentifiedValueObject {
    /**
     * 聚合类型
     */
    @Column(name = "aggregate_type", nullable = false, updatable = false)
    private String aggregateType;
    /**
     * 聚合唯一标识
     */
    @Column(name = "aggregate_id", nullable = false, updatable = false)
    private String aggregateId;
    /**
     * 事件唯一标识（不要加唯一约束，因为这张表不需要查询，消费者自己负责）
     */
    @Column(name = "event_id", nullable = false, updatable = false)
    private String eventId;
    /**
     * 事件时间
     */
    @Column(name = "event_time", nullable = false, updatable = false)
    private Long eventTime;
    /**
     * 事件类型
     */
    @Column(name = "event_type", nullable = false, updatable = false)
    private String eventType;
    /**
     * 事件内容
     */
    @Column(name = "payload", updatable = false)
    private String payload;

    protected Outbox() {
    }

    public Outbox(String aggregateType, String aggregateId, String eventId, Instant eventTime, String eventType, String payload) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventId = eventId;
        this.eventTime = eventTime.toEpochMilli();
        this.eventType = eventType;
        this.payload = payload;
    }
}
