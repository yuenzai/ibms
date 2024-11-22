package cn.ecosync.ibms.autoconfigure.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@AutoConfiguration
@ConditionalOnClass(JpaRepository.class)
@ConditionalOnProperty(prefix = "spring.data.jpa.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableJpaAuditing
@EnableJpaRepositories("cn.ecosync.ibms.jpa.repository")
@EntityScan({"cn.ecosync.ibms.domain", "cn.ecosync.ibms.jpa.converter"})
public class JpaAutoConfiguration {
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(JPAQueryFactory.class)
    public static class QueryDslJpaConfiguration {
        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }
}
