//package cn.ecosync.ibms.device.event;
//
//import cn.ecosync.ibms.device.dto.DeviceDataAcquisitionDtoBak;
//import cn.ecosync.ibms.device.model.DeviceDataAcquisitionId;
//import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
//import cn.ecosync.ibms.device.model.DeviceDataAcquisitionProperties;
//import cn.ecosync.ibms.device.model.DeviceGatewayId;
//import com.fasterxml.jackson.annotation.JsonUnwrapped;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import lombok.ToString;
//import org.springframework.util.Assert;
//
//@ToString
//public class DeviceDataAcquisitionSavedEvent extends DeviceDataAcquisitionEvent {
//    public static final String EVENT_TYPE = "device-daq-saved-event";
//
//    @JsonUnwrapped
//    @JsonDeserialize(as = DeviceDataAcquisitionDtoBak.class)
//    private DeviceDataAcquisition deviceDataAcquisitionModel;
//
//    protected DeviceDataAcquisitionSavedEvent() {
//    }
//
//    public DeviceDataAcquisitionSavedEvent(DeviceDataAcquisition deviceDataAcquisitionModel) {
//        Assert.notNull(deviceDataAcquisitionModel, "deviceDataAcquisitionModel must not be null");
//        this.deviceDataAcquisitionModel = deviceDataAcquisitionModel;
//    }
//
//    @Override
//    public String eventKey() {
//        return getDataAcquisitionId().toStringId();
//    }
//
//    @Override
//    public DeviceDataAcquisitionId getDataAcquisitionId() {
//        return deviceDataAcquisitionModel.getDataAcquisitionId();
//    }
//
//    @Override
//    public DeviceDataAcquisitionProperties getDaqProperties() {
//        return deviceDataAcquisitionModel.getDaqProperties();
//    }
//
//    @Override
//    public DeviceGatewayId getGatewayId() {
//        return deviceDataAcquisitionModel.getGatewayId();
//    }
//}
