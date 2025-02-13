package cn.ecosync.ibms.gateway.service;

import cn.ecosync.ibms.gateway.model.DeviceDataAcquisition;
import cn.ecosync.ibms.gateway.model.DeviceDataAcquisitionId;

import java.util.List;

public interface TelemetryService {
    void add(DeviceDataAcquisition... deviceDataAcquisitions);

    default void remove(DeviceDataAcquisition... deviceDataAcquisitions) {
        for (DeviceDataAcquisition deviceDataAcquisition : deviceDataAcquisitions) {
            this.remove(deviceDataAcquisition.getDataAcquisitionId());
        }
    }

    void remove(DeviceDataAcquisitionId deviceDataAcquisitionId);

    void set(List<DeviceDataAcquisition> dataAcquisitions);

    void reload();
}
