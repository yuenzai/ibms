package cn.ecosync.ibms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.Optional;

@Slf4j
@Service
public class JdbcServiceImpl implements JdbcService {
    @Override
    public Optional<String> getDatabaseName(DataSource dataSource) {
        if (dataSource != null) {
            try {
                return Optional.of(org.springframework.jdbc.support.JdbcUtils.commonDatabaseName(
                        org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseMetaData::getDatabaseProductName)
                ));
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return Optional.empty();
    }
}
