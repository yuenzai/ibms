package cn.ecosync.ibms.jpa;

import javax.sql.DataSource;
import java.util.Optional;

public interface JpaService {
    /**
     * 返回 JDBC 供应商的名称：<br>
     * Microsoft SQL Server<br>
     * Sybase Anywhere<br>
     * Oracle<br>
     * PostgreSQL<br>
     * H2<br>
     * HSQL Database Engine<br>
     * Apache Derby<br>
     * Ingres<br>
     * MySQL<br>
     * MariaDB<br>
     * SQLite<br>
     * Informix Dynamic Server<br>
     * DB2<br>
     * Firebird<br>
     * <a href="https://stackoverflow.com/questions/43868217/how-to-get-the-database-vendor-name-from-spring-jdbctemplate">
     * https://stackoverflow.com/questions/43868217/how-to-get-the-database-vendor-name-from-spring-jdbctemplate
     * </a>
     */
    Optional<String> getDatabaseName(DataSource dataSource);
}
