package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.query.SearchGatewayQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceGatewayJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SearchGatewayQueryHandler implements QueryHandler<SearchGatewayQuery, Page<DeviceGateway>> {
    private final DeviceGatewayJpaRepository gatewayRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceGateway> handle(SearchGatewayQuery query) {
        Pageable pageable = query.toPageable();
        return gatewayRepository.findAll(pageable)
                .map(DeviceGatewayEntity::getGateway);
    }
}
