package cn.ecosync.ibms.gateway.jpa;

import cn.ecosync.ibms.Constants;
import cn.ecosync.ibms.event.Event;
import cn.ecosync.ibms.gateway.command.SaveDataAcquisitionCommand;
import cn.ecosync.ibms.gateway.event.DeviceDataAcquisitionRemovedEvent;
import cn.ecosync.ibms.gateway.event.DeviceDataAcquisitionSavedEvent;
import cn.ecosync.ibms.gateway.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DeviceDataAcquisitionJpaRepository implements DeviceDataAcquisitionRepository {
    public static final String STATEMENT_INSERT = "INSERT INTO gateway_data_point (data_acquisition_id, device_code, metric_name) VALUES (?, ?, ?)";
    public static final String STATEMENT_DELETE = "DELETE FROM gateway_data_point WHERE data_acquisition_id = ?";

    private final DeviceDataAcquisitionDao dataAcquisitionDao;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Event> save(SaveDataAcquisitionCommand command) {
        DeviceDataAcquisition dataAcquisition;
        DeviceDataAcquisitionId dataAcquisitionId = command.getDataAcquisitionId();
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionDao.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity == null) {
            dataAcquisition = new DeviceDataAcquisition(dataAcquisitionId, command.getScrapeInterval(), command.getScrapeTimeout(), command.getDeviceInfos(), command.getDataPoints(), command.getSynchronizationState());
            dataAcquisitionEntity = new DeviceDataAcquisitionEntity(dataAcquisition);
            dataAcquisitionDao.save(dataAcquisitionEntity);
        } else {
            dataAcquisition = dataAcquisitionEntity.getPayload().builder()
                    .withScrapeInterval(command.getScrapeInterval())
                    .withScrapeTimeout(command.getScrapeTimeout())
                    .withDeviceInfos(command.getDeviceInfos())
                    .withDataPoints(command.getDataPoints())
                    .with(command.getSynchronizationState())
                    .build();
            dataAcquisitionEntity.setPayload(dataAcquisition);
        }
        if (command.getDataPoints() != null) {
            Integer id = dataAcquisitionEntity.id();
            jdbcTemplate.update(STATEMENT_DELETE, id);

            LabelTable dataPoints = dataAcquisition.getDataPoints();
            List<String> deviceCodes = dataPoints.get(Constants.LABEL_DEVICE_CODE)
                    .collect(Collectors.toList());
            List<String> metricNames = dataPoints.get(Constants.LABEL_METRIC_NAME)
                    .collect(Collectors.toList());

            Collection<DeviceDataPointId> dataPointsIds = new ArrayList<>(dataPoints.size());
            for (int i = 0; i < dataPoints.size(); i++) {
                dataPointsIds.add(new DeviceDataPointId(metricNames.get(i), deviceCodes.get(i)));
            }

            jdbcTemplate.batchUpdate(STATEMENT_INSERT, dataPointsIds, 100, (ps, in) -> {
                ps.setInt(1, id);
                ps.setString(2, in.getDeviceCode());
                ps.setString(3, in.getMetricName());
            });
        }
        DeviceDataAcquisitionSavedEvent event = new DeviceDataAcquisitionSavedEvent(dataAcquisition);
        return Collections.singletonList(event);
    }

    @Override
    public Collection<Event> remove(DeviceDataAcquisitionId dataAcquisitionId) {
        DeviceDataAcquisitionEntity dataAcquisitionEntity = dataAcquisitionDao.findByDataAcquisitionId(dataAcquisitionId).orElse(null);
        if (dataAcquisitionEntity != null) {
            DeviceDataAcquisition dataAcquisition = dataAcquisitionEntity.getPayload();
            jdbcTemplate.update(STATEMENT_DELETE, dataAcquisitionEntity.id());
            dataAcquisitionDao.delete(dataAcquisitionEntity);
            DeviceDataAcquisitionRemovedEvent event = new DeviceDataAcquisitionRemovedEvent(dataAcquisition);
            return Collections.singletonList(event);
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
