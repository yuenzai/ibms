package cn.ecosync.ibms.device.jpa;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.device.model.DeviceDataPoint;
import cn.ecosync.ibms.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeviceDataAcquisitionJpaRepository implements DeviceDataAcquisitionRepository {
    public static final String STATEMENT_INSERT = "INSERT INTO device_data_point (data_acquisition_id, device_code, metric_name) VALUES (?, ?, ?)";
    public static final String STATEMENT_DELETE = "DELETE FROM device_data_point WHERE data_acquisition_id = ?";

    private final DeviceDataAcquisitionDao dataAcquisitionDao;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(DeviceDataAcquisition dataAcquisition) {
        DeviceDataAcquisitionId dataAcquisitionId = dataAcquisition.getDataAcquisitionId();
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionDao.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity == null) {
            dataAcquisitionEntity = new DeviceDataAcquisitionEntity(dataAcquisition);
            dataAcquisitionDao.save(dataAcquisitionEntity);
        } else {
            dataAcquisitionEntity.setPayload(dataAcquisition);
            Integer id = dataAcquisitionEntity.id;
            jdbcTemplate.update(STATEMENT_DELETE, id);
            Collection<? extends DeviceDataPoint> dataPoints = dataAcquisition.getDataPoints().toCollection();
            if (CollectionUtils.notEmpty(dataPoints)) {
                jdbcTemplate.batchUpdate(STATEMENT_INSERT, dataPoints, 100, (ps, in) -> {
                    ps.setInt(1, id);
                    ps.setString(2, in.getDataPointId().getDeviceCode());
                    ps.setString(3, in.getDataPointId().getMetricName());
                });
            }
        }
    }

    @Override
    public void remove(DeviceDataAcquisitionId dataAcquisitionId) {
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionDao.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity == null) return;
        jdbcTemplate.update(STATEMENT_DELETE, dataAcquisitionEntity.id);
        dataAcquisitionDao.delete(dataAcquisitionEntity);
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
