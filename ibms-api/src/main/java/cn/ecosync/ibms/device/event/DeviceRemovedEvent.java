//package cn.ecosync.ibms.device.event;
//
//import cn.ecosync.ibms.device.dto.DeviceDtoBak;
//import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
//import cn.ecosync.ibms.device.model.DeviceId;
//import cn.ecosync.ibms.device.model.Device;
//import cn.ecosync.ibms.device.model.DeviceProperties;
//import com.fasterxml.jackson.annotation.JsonUnwrapped;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import lombok.ToString;
//import org.springframework.util.Assert;
//
//@ToString
//public class DeviceRemovedEvent extends DeviceEvent {
//    public static final String EVENT_TYPE = "device-removed-event";
//
//    @JsonUnwrapped
//    @JsonDeserialize(as = DeviceDtoBak.class)
//    private Device device;
//
//    protected DeviceRemovedEvent() {
//    }
//
//    public DeviceRemovedEvent(Device device) {
//        Assert.notNull(device, "device must not be null");
//        this.device = device;
//    }
//
//    @Override
//    public DeviceEventAggregator apply(DeviceEventAggregator functions) {
//        functions.onApply(this);
//        return functions;
//    }
//
//    @Override
//    public String eventKey() {
//        return getDeviceId().toStringId();
//    }
//
//    @Override
//    public DeviceId getDeviceId() {
//        return device.getDeviceId();
//    }
//
//    @Override
//    public DeviceDataAcquisitionId getDaqId() {
//        return device.getDaqId();
//    }
//
//    @Override
//    public DeviceProperties getDeviceProperties() {
//        return device.getDeviceProperties();
//    }
//}
