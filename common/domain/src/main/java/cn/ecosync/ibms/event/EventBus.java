package cn.ecosync.ibms.event;

/**
 * @author 覃俊元
 * @since 2024
 */
public interface EventBus {
    /**
     * 发布事件
     * “发布”意味着事件是由消费者决定事件怎么处理
     *
     * @param event 事件
     */
    void publish(Event event);

    /**
     * 处理事件
     * “处理”意味着事件是由生产者决定事件怎么处理
     *
     * @param event 事件
     */
    void handle(Event event);
}
