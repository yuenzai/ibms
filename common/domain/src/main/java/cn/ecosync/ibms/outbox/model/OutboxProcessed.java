package cn.ecosync.ibms.outbox.model;

import cn.ecosync.ibms.model.IdentifiedValueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "outbox_processed")
public class OutboxProcessed extends IdentifiedValueObject {
    /**
     * 事件唯一标识
     */
    @Column(name = "event_id", nullable = false, updatable = false, unique = true)
    private String eventId;

    public OutboxProcessed() {
    }

    public OutboxProcessed(String eventId) {
        this.eventId = eventId;
    }
}
