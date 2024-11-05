package cn.ecosync.ibms.event;

/**
 * @author 覃俊元
 * @since 2024
 */
public interface EventBus {
    void publish(Event event);

    void handle(Event event);
}
