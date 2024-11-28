package cn.ecosync.ibms.scheduling.repository;

import cn.ecosync.ibms.scheduling.domain.SchedulingReadonlyRepository;
import cn.ecosync.ibms.scheduling.dto.SchedulingDto;
import cn.ecosync.ibms.scheduling.dto.SchedulingTaskParams;
import cn.ecosync.ibms.scheduling.dto.SchedulingTrigger;
import cn.ecosync.iframework.serde.JsonSerde;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

public class SchedulingReadonlyJdbcRepository implements SchedulingReadonlyRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<SchedulingDto> rowMapper;

    private static final String SQL_SELECT = "SELECT scheduling_name, scheduling_trigger, scheduling_task_params, description, created_date, last_modified_date FROM scheduling order by id desc";

    public SchedulingReadonlyJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate, JsonSerde jsonSerde) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = (resultSet, rowNum) -> {
            SchedulingTrigger schedulingTrigger = jsonSerde.deserialize(resultSet.getString("scheduling_trigger"), SchedulingTrigger.class);
            SchedulingTaskParams schedulingTaskParams = jsonSerde.deserialize(resultSet.getString("scheduling_task_params"), SchedulingTaskParams.class);
            return new SchedulingDto(
                    resultSet.getString("scheduling_name"),
                    schedulingTrigger,
                    schedulingTaskParams,
                    resultSet.getString("description"),
                    resultSet.getLong("created_date"),
                    resultSet.getLong("last_modified_date")
            );
        };
    }

    @Override
    public List<SchedulingDto> listing() {
        return jdbcTemplate.query(SQL_SELECT, rowMapper);
    }

    @Override
    public Page<SchedulingDto> paging(Pageable pageable) {
        Assert.notNull(pageable, "pageable must not be null");
        Long total = jdbcTemplate.queryForObject("SELECT count(*) FROM scheduling", new EmptySqlParameterSource(), Long.class);

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("limit", pageable.getPageSize());
        namedParameters.addValue("offset", pageable.getOffset());
        List<SchedulingDto> schedules = jdbcTemplate.query(SQL_SELECT + " LIMIT :limit OFFSET :offset", namedParameters, rowMapper);

        return new PageImpl<>(schedules, pageable, Optional.ofNullable(total).orElse(0L));
    }
}
