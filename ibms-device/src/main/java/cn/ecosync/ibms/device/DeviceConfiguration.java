package cn.ecosync.ibms.device;

import cn.ecosync.ibms.device.command.handler.RemoveDataAcquisitionCommandHandler;
import cn.ecosync.ibms.device.command.handler.SaveDataAcquisitionCommandHandler;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.device.query.handler.GetDataAcquisitionQueryHandler;
import cn.ecosync.ibms.device.query.handler.SearchDataAcquisitionQueryHandler;
import cn.ecosync.ibms.event.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceConfiguration {
    @Bean
    public SaveDataAcquisitionCommandHandler saveDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        return new SaveDataAcquisitionCommandHandler(dataAcquisitionRepository, eventBus);
    }

    @Bean
    public RemoveDataAcquisitionCommandHandler removeDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository, EventBus eventBus) {
        return new RemoveDataAcquisitionCommandHandler(dataAcquisitionRepository, eventBus);
    }

    @Bean
    public GetDataAcquisitionQueryHandler getDataAcquisitionQueryHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        return new GetDataAcquisitionQueryHandler(dataAcquisitionRepository);
    }

    @Bean
    public SearchDataAcquisitionQueryHandler searchDataAcquisitionQueryHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        return new SearchDataAcquisitionQueryHandler(dataAcquisitionRepository);
    }
}
