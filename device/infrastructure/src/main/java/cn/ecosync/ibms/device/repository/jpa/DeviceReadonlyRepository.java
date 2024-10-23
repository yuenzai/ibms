package cn.ecosync.ibms.device.repository.jpa;

import cn.ecosync.ibms.device.DeviceConstant;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.device.model.DeviceExtra;
import cn.ecosync.ibms.device.model.DevicePointDto;
import cn.ecosync.ibms.device.model.DeviceStatus;
import cn.ecosync.ibms.serde.JsonSerde;
import cn.ecosync.ibms.serde.TypeReference;
import cn.ecosync.ibms.util.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class DeviceReadonlyRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<DeviceDto> rowMapper;
    private final String SQL_SELECT = "SELECT " + String.join(", ", DeviceConstant.FIELDS) + " FROM device_readonly";

    public DeviceReadonlyRepository(JdbcTemplate jdbcTemplate, JsonSerde jsonSerde) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = (resultSet, rowNum) -> {
            DeviceExtra deviceExtra = jsonSerde.readValue(resultSet.getString("device_extra"), DeviceExtra.class).orElse(null);
            List<DevicePointDto> devicePoints = jsonSerde.readValue(resultSet.getString("device_points"), new TypeReference<List<DevicePointDto>>() {
            }).orElse(Collections.emptyList());
            DeviceStatus deviceStatus = jsonSerde.readValue(resultSet.getString("device_status"), DeviceStatus.class).orElse(null);
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

    public Optional<DeviceDto> findByDeviceCode(String deviceCode) {
        String sqlStatement = SQL_SELECT + " WHERE device_code = ?";
        DeviceDto deviceDto = CollectionUtils.firstElement(jdbcTemplate.query(sqlStatement, rowMapper, deviceCode));
        return Optional.ofNullable(deviceDto);
    }

    public List<DeviceDto> findAll() {
        String sqlStatement = SQL_SELECT;
        return jdbcTemplate.query(sqlStatement, rowMapper);
    }

    public Page<DeviceDto> findAll(Pageable pageable) {
        String sqlStatement = SQL_SELECT + " LIMIT ? OFFSET ?";
        Long total = jdbcTemplate.queryForObject("SELECT count(*) FROM device_readonly", Long.class);
        List<DeviceDto> deviceDtos = jdbcTemplate.query(sqlStatement, rowMapper, pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<>(deviceDtos, pageable, Optional.ofNullable(total).orElse(0L));
    }
}
