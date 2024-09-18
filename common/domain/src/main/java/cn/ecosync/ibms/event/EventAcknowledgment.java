package cn.ecosync.ibms.event;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EventAcknowledgment {
    void acknowledge(String eventId);
}
