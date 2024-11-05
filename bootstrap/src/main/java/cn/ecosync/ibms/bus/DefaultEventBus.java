package cn.ecosync.ibms.bus;

import cn.ecosync.ibms.event.AbstractEventBus;
import cn.ecosync.ibms.event.Event;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuenzai
 * @since 2024
 */
@Slf4j
public class DefaultEventBus extends AbstractEventBus {
    @Override
    public void publish(Event event) {
        handle(event);
    }
}
