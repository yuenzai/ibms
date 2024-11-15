package cn.ecosync.ibms.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.Optional;

@Slf4j
public class DefaultJpaService implements JpaService {
    @Override
    public Optional<String> getDatabaseName(DataSource dataSource) {
        if (dataSource == null) {
            return Optional.empty();
        }
        try {
            String databaseProductName = JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseMetaData::getDatabaseProductName);
            return Optional.of(JdbcUtils.commonDatabaseName(databaseProductName));
        } catch (Exception e) {
            log.error("", e);
            return Optional.empty();
        }
    }
}
