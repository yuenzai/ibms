package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.device.query.GetGatewayQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceGatewayJpaRepository;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetGatewayQueryHandler implements QueryHandler<GetGatewayQuery, DeviceGateway> {
    private final DeviceGatewayJpaRepository gatewayRepository;
    private final QueryBus queryBus;

    @Override
    @Transactional(readOnly = true)
    public DeviceGateway handle(GetGatewayQuery query) {
        DeviceGatewayId gatewayId = new DeviceGatewayId(query.getGatewayCode());
        DeviceGatewayEntity gatewayEntity = gatewayRepository.findByGatewayId(gatewayId).orElse(null);
        if (gatewayEntity == null) return null;

        List<DeviceDataAcquisition> dataAcquisitions = gatewayEntity.getDataAcquisitions().stream()
                .map(DeviceDataAcquisition::getDataAcquisitionId)
                .map(GetDataAcquisitionQuery::new)
                .map(queryBus::execute)
                .collect(Collectors.toList());

        return gatewayEntity.getGateway()
                .withDataAcquisitions(dataAcquisitions);
    }
}
