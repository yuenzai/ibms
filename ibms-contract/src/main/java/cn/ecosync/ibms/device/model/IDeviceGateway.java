package cn.ecosync.ibms.device.model;

import java.util.List;

public interface IDeviceGateway {
    List<? extends IDeviceDataAcquisition> getDataAcquisitions();
}
