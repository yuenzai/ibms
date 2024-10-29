package cn.ecosync.ibms.event;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yuenzai
 * @since 2024
 */
@Slf4j
public class TransactionalEventBus extends AbstractEventBus {
    @Override
    public void publish(Event event) {
        handle(event);
    }
}
