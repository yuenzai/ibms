package cn.ecosync.ibms.event;

/**
 * @author yuenzai
 * @since 2024
 */
public interface Event {
    String eventId();

    String eventDestination();

    String eventKey();

    Long eventTime();
}
