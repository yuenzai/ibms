package cn.ecosync.ibms.outbox.model;

import cn.ecosync.ibms.model.IdentifiedValueObject;

public class OutboxProcessed extends IdentifiedValueObject {
    /**
     * 事件唯一标识
     */
    private String eventId;

    public OutboxProcessed() {
    }

    public OutboxProcessed(String eventId) {
        this.eventId = eventId;
    }
}
