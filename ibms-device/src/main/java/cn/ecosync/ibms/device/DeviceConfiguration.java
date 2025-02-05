package cn.ecosync.ibms.device;

import cn.ecosync.ibms.device.command.handler.RemoveDataAcquisitionCommandHandler;
import cn.ecosync.ibms.device.command.handler.SaveDataAcquisitionCommandHandler;
import cn.ecosync.ibms.device.model.DeviceDataAcquisitionRepository;
import cn.ecosync.ibms.device.query.handler.GetDataAcquisitionQueryHandler;
import cn.ecosync.ibms.device.query.handler.SearchDataAcquisitionQueryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceConfiguration {
    @Bean
    public SaveDataAcquisitionCommandHandler saveDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        return new SaveDataAcquisitionCommandHandler(dataAcquisitionRepository);
    }

    @Bean
    public RemoveDataAcquisitionCommandHandler removeDataAcquisitionCommandHandler(DeviceDataAcquisitionRepository dataAcquisitionRepository) {
        return new RemoveDataAcquisitionCommandHandler(dataAcquisitionRepository);
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
