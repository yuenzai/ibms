package cn.ecosync.ibms.device.model;

import cn.ecosync.ibms.device.DeviceConstant;
import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.IdentifiedValueObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "device_readonly")
public class DeviceDto extends IdentifiedValueObject implements AggregateRoot {
    private String deviceCode;
    private String deviceName;
    private String path;
    private String description;
    private DeviceExtra deviceExtra;
    private Boolean enabled;
    private List<DevicePointDto> devicePoints;
    private List<DevicePointValueDto> deviceStatus;

    @Override
    public String aggregateType() {
        return DeviceConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return deviceCode;
    }
}
