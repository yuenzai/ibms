package cn.ecosync.ibms.device.model;

import java.util.List;

public interface TelemetryService {
    void add(DeviceDataAcquisition deviceDataAcquisition);

    void remove(DeviceDataAcquisitionId deviceDataAcquisitionId);

    void set(List<DeviceDataAcquisition> dataAcquisitions);

    void reload();
}
