//package cn.ecosync.ibms.device.event.handler;
//
//import cn.ecosync.ibms.device.domain.DeviceReadonlyRepository;
//import cn.ecosync.ibms.device.dto.DeviceDto;
//import cn.ecosync.ibms.device.event.DeviceStatusUpdatedEvent;
//import cn.ecosync.iframework.event.AggregateRemovedEvent;
//import cn.ecosync.iframework.event.AggregateSavedEvent;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//
/// **
// * AggregateRemovedEvent没用了
// */
//@Deprecated
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DeviceModelSynchronizer {
//    private final DeviceReadonlyRepository deviceReadonlyRepository;
//
//    @Transactional
//    @EventListener(condition = "#event.aggregateType() == 'device'")
//    public void onEvent(AggregateSavedEvent event) {
//        log.info("onEvent: {}", event);
//        Assert.isInstanceOf(DeviceDto.class, event.aggregateRoot(), "");
//        DeviceDto deviceDto = (DeviceDto) event.aggregateRoot();
//        deviceReadonlyRepository.save(deviceDto);
//    }
//
//    @Transactional
//    @EventListener(condition = "#event.aggregateType() == 'device'")
//    public void onEvent(AggregateRemovedEvent event) {
//        log.info("onEvent: {}", event);
//        deviceReadonlyRepository.remove(event.aggregateId());
//    }
//
//    @Transactional
//    @EventListener
//    public void onEvent(DeviceStatusUpdatedEvent event) {
//        log.debug("onEvent: {}", event);
//        deviceReadonlyRepository.update(event.getDeviceStatus());
//    }
//}
