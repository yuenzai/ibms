package cn.ecosync.ibms.repository.jdbc;

import cn.ecosync.ibms.dto.DeviceDto;
import cn.ecosync.ibms.dto.DeviceExtra;
import cn.ecosync.ibms.dto.DevicePointDto;
import cn.ecosync.ibms.dto.DeviceStatus;
import cn.ecosync.ibms.domain.DeviceReadonlyRepository;
import cn.ecosync.iframework.serde.JsonSerde;
import cn.ecosync.iframework.serde.TypeReference;
import cn.ecosync.iframework.util.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DeviceReadonlyJdbcRepository implements DeviceReadonlyRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JsonSerde jsonSerde;
    private final RowMapper<DeviceDto> rowMapper;

    private static final String SQL_SELECT = "SELECT device_code, device_name, path, description, enabled, device_extra, device_points, device_status FROM device_readonly";
    private static final String SQL_INSERT = "INSERT INTO device_readonly (device_code, device_name, path, description, enabled, device_extra, device_points) VALUES (:deviceCode, :deviceName, :path, :description, :enabled, :deviceExtra, :devicePoints)";
    private static final String SQL_DELETE = "DELETE FROM device_readonly WHERE device_code = :deviceCode";
    private static final String SQL_UPDATE = "UPDATE device_readonly SET device_name = :deviceName, path = :path, description = :description, enabled = :enabled, device_extra = :deviceExtra, device_points = :devicePoints WHERE device_code = :deviceCode";
    private static final String SQL_UPDATE_STATUS = "UPDATE device_readonly SET device_status = :deviceStatus WHERE device_code = :deviceCode";

    public DeviceReadonlyJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate, JsonSerde jsonSerde) {
        this.jdbcTemplate = jdbcTemplate;
        this.jsonSerde = jsonSerde;
        rowMapper = (resultSet, rowNum) -> {
            DeviceExtra deviceExtra = jsonSerde.deserialize(resultSet.getString("device_extra"), DeviceExtra.class);
            List<DevicePointDto> devicePoints = Optional.ofNullable(jsonSerde.deserialize(resultSet.getString("device_points"), new TypeReference<List<DevicePointDto>>() {
            })).orElse(Collections.emptyList());
            DeviceStatus deviceStatus = jsonSerde.deserialize(resultSet.getString("device_status"), DeviceStatus.class);
            return new DeviceDto(
                    resultSet.getString("device_code"),
                    resultSet.getString("device_name"),
                    resultSet.getString("path"),
                    resultSet.getString("description"),
                    resultSet.getBoolean("enabled"),
                    deviceExtra,
                    devicePoints,
                    deviceStatus
            );
        };
    }

    @Override
    public Optional<DeviceDto> findByDeviceCode(String deviceCode) {
        Assert.hasText(deviceCode, "deviceCode must not be empty");
        Map<String, String> namedParameters = Collections.singletonMap("deviceCode", deviceCode);
        DeviceDto deviceDto = CollectionUtils.firstElement(
                jdbcTemplate.query(SQL_SELECT + " WHERE device_code = :deviceCode", namedParameters, rowMapper)
        );
        return Optional.ofNullable(deviceDto);
    }

    @Override
    public List<DeviceDto> findAll() {
        return jdbcTemplate.query(SQL_SELECT, rowMapper);
    }

    @Override
    public Page<DeviceDto> findAll(Pageable pageable) {
        Assert.notNull(pageable, "pageable must not be null");
        Long total = jdbcTemplate.queryForObject("SELECT count(*) FROM device_readonly", new EmptySqlParameterSource(), Long.class);

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("limit", pageable.getPageSize());
        namedParameters.addValue("offset", pageable.getOffset());
        List<DeviceDto> deviceDtos = jdbcTemplate.query(SQL_SELECT + " LIMIT :limit OFFSET :offset", namedParameters, rowMapper);

        return new PageImpl<>(deviceDtos, pageable, Optional.ofNullable(total).orElse(0L));
    }

    @Override
    public void save(DeviceDto deviceDto) {
        Assert.notNull(deviceDto, "deviceDto must not be null");

        String countSql = "SELECT count(*) FROM device_readonly WHERE device_code = :deviceCode";
        SqlParameterSource namedParameters = new MapSqlParameterSource("deviceCode", deviceDto.getDeviceCode());
        Integer count = jdbcTemplate.queryForObject(countSql, namedParameters, Integer.class);

        namedParameters = toNamedParameters(deviceDto);
        if (Optional.ofNullable(count).orElse(0) > 0) {
            jdbcTemplate.update(SQL_UPDATE, namedParameters);
        } else {
            jdbcTemplate.update(SQL_INSERT, namedParameters);
        }
    }

    @Override
    public void remove(String deviceCode) {
        Assert.hasText(deviceCode, "deviceCode must not be empty");

        SqlParameterSource namedParameters = new MapSqlParameterSource("deviceCode", deviceCode);
        jdbcTemplate.update(SQL_DELETE, namedParameters);
    }

    @Override
    public void update(DeviceStatus deviceStatus) {
        Assert.notNull(deviceStatus, "deviceStatus must not be null");

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("deviceCode", deviceStatus.getDeviceCode());
        namedParameters.addValue("deviceStatus", jsonSerde.serialize(deviceStatus));
        jdbcTemplate.update(SQL_UPDATE_STATUS, namedParameters);
    }

    private SqlParameterSource toNamedParameters(DeviceDto deviceDto) {
        String deviceCode = deviceDto.getDeviceCode();
        String deviceName = deviceDto.getDeviceName();
        String path = deviceDto.getPath();
        String description = deviceDto.getDescription();
        String deviceExtra = Optional.ofNullable(jsonSerde.serialize(deviceDto.getDeviceExtra())).orElse("{}");
        Boolean enabled = deviceDto.getEnabled();
        String devicePoints = Optional.ofNullable(jsonSerde.serialize(deviceDto.getDevicePoints())).orElse("[]");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("device_code", deviceCode);
        namedParameters.addValue("device_name", deviceName);
        namedParameters.addValue("path", path);
        namedParameters.addValue("description", description);
        namedParameters.addValue("enabled", enabled);
        namedParameters.addValue("device_extra", deviceExtra);
        namedParameters.addValue("device_points", devicePoints);
        namedParameters.addValue("device_status", deviceDto.getDeviceStatus());
        return namedParameters;
    }
}
