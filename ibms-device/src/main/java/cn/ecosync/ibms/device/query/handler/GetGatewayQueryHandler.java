package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.model.DeviceGatewayId;
import cn.ecosync.ibms.device.query.GetGatewayQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceGatewayJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetGatewayQueryHandler implements QueryHandler<GetGatewayQuery, DeviceGateway> {
    private final DeviceGatewayJpaRepository gatewayRepository;

    @Override
    @Transactional(readOnly = true)
    public DeviceGateway handle(GetGatewayQuery query) {
        DeviceGatewayId gatewayId = new DeviceGatewayId(query.getGatewayCode());
        return gatewayRepository.findByGatewayId(gatewayId)
                .map(DeviceGatewayEntity::getGateway)
                .orElse(null);
    }
}
