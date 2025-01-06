package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.model.DeviceGateway;
import cn.ecosync.ibms.device.jpa.DeviceGatewayEntity;
import cn.ecosync.ibms.device.query.SearchGatewayQuery;
import cn.ecosync.ibms.device.repository.jpa.DeviceGatewayJpaRepository;
import cn.ecosync.iframework.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchGatewayQueryHandler implements QueryHandler<SearchGatewayQuery, List<DeviceGateway>> {
    private final DeviceGatewayJpaRepository gatewayRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceGateway> handle(SearchGatewayQuery query) {
        Pageable pageable = query.toPageable();
        List<DeviceGatewayEntity> gatewayEntities = pageable.isPaged()
                ? gatewayRepository.findAll(pageable).getContent()
                : gatewayRepository.findAll(pageable.getSort());
        return gatewayEntities.stream()
                .map(DeviceGatewayEntity::getGateway)
                .collect(Collectors.toList());
    }
}
