package cn.ecosync.ibms.device.event.handler;

import cn.ecosync.ibms.JdbcService;
import cn.ecosync.ibms.device.event.DeviceStatusUpdatedEvent;
import cn.ecosync.ibms.device.model.DeviceDto;
import cn.ecosync.ibms.event.AggregateRemovedEvent;
import cn.ecosync.ibms.event.AggregateSavedEvent;
import cn.ecosync.ibms.serde.JsonSerde;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.sql.DataSource;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnClass(JdbcTemplate.class)
public class DeviceModelSynchronizer {
    private static final String STATEMENT_MYSQL =
            "INSERT INTO device_readonly (device_code, device_name, path, description, enabled, device_extra, device_points) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) AS new " +
                    "ON DUPLICATE KEY UPDATE " +
                    "device_name = new.device_name, " +
                    "path = new.path, " +
                    "description = new.description, " +
                    "enabled = new.enabled, " +
                    "device_extra = new.device_extra, " +
                    "device_points = new.device_points";

    private final JdbcTemplate jdbcTemplate;
    private final JdbcService jdbcService;
    private final JsonSerde jsonSerde;

    @Transactional
    @EventListener(condition = "#event.aggregateType() == 'device'")
    public void onEvent(AggregateSavedEvent event) {
        log.info("onEvent: {}", event);
        Assert.isInstanceOf(DeviceDto.class, event.aggregateRoot(), "");
        DeviceDto deviceDto = (DeviceDto) event.aggregateRoot();
        DataSource dataSource = jdbcTemplate.getDataSource();
        String vendor = jdbcService.getDatabaseName(dataSource).orElse(null);
        if ("MySQL".equals(vendor)) {
            jdbcTemplate.update(STATEMENT_MYSQL, args(deviceDto));
        }
    }

    @Transactional
    @EventListener(condition = "#event.aggregateType() == 'device'")
    public void onEvent(AggregateRemovedEvent event) {
        log.info("onEvent: {}", event);
        jdbcTemplate.update("DELETE FROM device_readonly WHERE device_code = ?", event.aggregateId());
    }

    @Transactional
    @EventListener
    public void onEvent(DeviceStatusUpdatedEvent event) {
        log.debug("onEvent: {}", event);
        String deviceStatus = jsonSerde.writeValueAsString(event.getDeviceStatus()).orElse(null);
        if (deviceStatus == null) {
            return;
        }
        jdbcTemplate.update("UPDATE device_readonly SET device_status = ? WHERE device_code = ?", deviceStatus, event.getDeviceCode());
    }

    private Object[] args(DeviceDto deviceDto) {
        String deviceCode = deviceDto.getDeviceCode();
        String deviceName = deviceDto.getDeviceName();
        String path = deviceDto.getPath();
        String description = deviceDto.getDescription();
        String deviceExtra = jsonSerde.writeValueAsString(deviceDto.getDeviceExtra()).orElse("{}");
        Boolean enabled = deviceDto.getEnabled();
        String devicePoints = jsonSerde.writeValueAsString(deviceDto.getDevicePoints()).orElse("[]");
        return new Object[]{deviceCode, deviceName, path, description, enabled, deviceExtra, devicePoints};
    }
}
