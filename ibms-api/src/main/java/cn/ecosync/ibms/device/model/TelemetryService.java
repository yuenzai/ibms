package cn.ecosync.ibms.device.model;

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
