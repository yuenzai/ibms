package cn.ecosync.ibms.device.query.handler;

import cn.ecosync.ibms.device.jpa.DeviceSchemasEntity;
import cn.ecosync.ibms.device.model.DeviceSchemas;
import cn.ecosync.ibms.device.model.DeviceSchemasId;
import cn.ecosync.ibms.device.query.GetSchemasQuery;
import cn.ecosync.ibms.device.jpa.DeviceSchemasJpaRepository;
import cn.ecosync.ibms.query.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetSchemasQueryHandler implements QueryHandler<GetSchemasQuery, DeviceSchemas> {
    private final DeviceSchemasJpaRepository schemasRepository;

    @Override
    @Transactional(readOnly = true)
    public DeviceSchemas handle(GetSchemasQuery query) {
        DeviceSchemasId schemasId = new DeviceSchemasId(query.getSchemasCode());
        return schemasRepository.findBySchemasId(schemasId)
                .map(DeviceSchemasEntity::getDeviceSchemas)
                .orElse(null);
    }
}
