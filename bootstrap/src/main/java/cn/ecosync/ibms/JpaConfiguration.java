package cn.ecosync.ibms;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories("cn.ecosync.**.repository.jpa")
@ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class, JPAQueryFactory.class})
public class JpaConfiguration {
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
