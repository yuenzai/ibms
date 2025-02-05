package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.device.event.DeviceDataAcquisitionChangedEvent;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.device.model.DeviceDataPoint;
import cn.ecosync.ibms.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeviceDataAcquisitionJpaRepository implements DeviceDataAcquisitionRepository {
    public static final String STATEMENT_INSERT = "INSERT INTO device_data_point (data_acquisition_id, device_code, metric_name) VALUES (?, ?, ?)";
    public static final String STATEMENT_DELETE = "DELETE FROM device_data_point WHERE data_acquisition_id = ?";

    private final DeviceDataAcquisitionDao dataAcquisitionDao;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Event> save(SaveDataAcquisitionCommand command) {
        DeviceDataAcquisition dataAcquisition;
        DeviceDataAcquisitionId dataAcquisitionId = command.getDataAcquisitionId();
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionDao.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity == null) {
            dataAcquisition = new DeviceDataAcquisition(dataAcquisitionId, command.getScrapeInterval(), command.getDataPoints(), command.getSynchronizationState());
            dataAcquisitionEntity = new DeviceDataAcquisitionEntity(dataAcquisition);
            dataAcquisitionDao.save(dataAcquisitionEntity);
        } else {
            dataAcquisition = dataAcquisitionEntity.getPayload().builder()
                    .with(command.getScrapeInterval())
                    .with(command.getDataPoints())
                    .with(command.getSynchronizationState())
                    .build();
            dataAcquisitionEntity.setPayload(dataAcquisition);
            if (command.getDataPoints() != null) {
                Integer id = dataAcquisitionEntity.id;
                jdbcTemplate.update(STATEMENT_DELETE, id);
                Collection<? extends DeviceDataPoint> dataPoints = dataAcquisition.getDataPoints().toCollection();
                jdbcTemplate.batchUpdate(STATEMENT_INSERT, dataPoints, 100, (ps, in) -> {
                    ps.setInt(1, id);
                    ps.setString(2, in.getDataPointId().getDeviceCode());
                    ps.setString(3, in.getDataPointId().getMetricName());
                });
            }
        }
        DeviceDataAcquisitionChangedEvent event = new DeviceDataAcquisitionChangedEvent(dataAcquisition);
        return Collections.singletonList(event);
    }

    @Override
    public Collection<Event> remove(DeviceDataAcquisitionId dataAcquisitionId) {
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionDao.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity != null) {
            jdbcTemplate.update(STATEMENT_DELETE, dataAcquisitionEntity.id);
            dataAcquisitionDao.delete(dataAcquisitionEntity);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<DeviceDataAcquisition> get(DeviceDataAcquisitionId dataAcquisitionId) {
        return dataAcquisitionDao.findByDataAcquisitionId(dataAcquisitionId)
                .map(DeviceDataAcquisitionEntity::getPayload);
    }

    @Override
    public Page<DeviceDataAcquisition> search(Pageable pageable) {
        return dataAcquisitionDao.findAll(pageable)
                .map(DeviceDataAcquisitionEntity::getPayload);
    }
}
