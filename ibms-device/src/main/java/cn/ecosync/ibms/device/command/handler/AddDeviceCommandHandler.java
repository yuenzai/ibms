//package cn.ecosync.ibms.device.command.handler;
//
//import cn.ecosync.ibms.device.command.AddDeviceCommand;
//import cn.ecosync.ibms.device.event.DeviceSavedEvent;
//import cn.ecosync.ibms.device.model.*;
//import cn.ecosync.iframework.command.CommandHandler;
//import cn.ecosync.iframework.event.EventBus;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//
//@Component
//@RequiredArgsConstructor
//public class AddDeviceCommandHandler implements CommandHandler<AddDeviceCommand> {
//    private final DeviceRepository<DeviceModel> deviceRepository;
//    private final EventBus eventBus;
//
//    @Override
//    @Transactional
//    public void handle(AddDeviceCommand command) {
//        DeviceId deviceId = command.getDeviceId();
//        DeviceCommandModel device = deviceRepository.get(deviceId).orElse(null);
//        Assert.isNull(device, "device already exists: " + deviceId.getDeviceCode());
//        device = new Device(deviceId, command.getDeviceProperties());
//        deviceRepository.add(device);
//        DeviceSavedEvent event = new DeviceSavedEvent(device);
//        eventBus.publish(event);
//    }
//}
