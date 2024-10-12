package cn.ecosync.ibms.device.dto;

import cn.ecosync.ibms.device.DeviceConstant;
import cn.ecosync.ibms.device.jpa.DevicePointDtoJpaConverter;
import cn.ecosync.ibms.device.jpa.DevicePointValueDtoJpaConverter;
import cn.ecosync.ibms.device.model.DeviceId;
import cn.ecosync.ibms.device.model.DeviceProperties;
import cn.ecosync.ibms.model.AggregateRoot;
import cn.ecosync.ibms.model.IdentifiedValueObject;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "device_readonly")
@AllArgsConstructor
public class DeviceDto extends IdentifiedValueObject implements AggregateRoot {
    @Embedded
    @JsonUnwrapped
    private DeviceId deviceId;

    @Embedded
    @JsonUnwrapped
    private DeviceProperties deviceProperties;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Convert(converter = DevicePointDtoJpaConverter.class)
    @Column(name = "device_points", nullable = false)
    private List<DevicePointDto> devicePoints;

    @Convert(converter = DevicePointValueDtoJpaConverter.class)
    @Column(name = "device_status", nullable = false)
    private List<DevicePointValueDto> deviceStatus;

    protected DeviceDto() {
    }

    public DeviceDto(DeviceId deviceId, DeviceProperties deviceProperties) {
        Assert.notNull(deviceId, "deviceId must not be null");
        Assert.notNull(deviceProperties, "deviceProperties must not be null");
        this.deviceId = deviceId;
        this.deviceProperties = deviceProperties;
        this.enabled = Boolean.TRUE;
        this.devicePoints = Collections.emptyList();
        this.deviceStatus = Collections.emptyList();
    }

    public void setDeviceProperties(DeviceProperties deviceProperties) {
        if (deviceProperties != null) {
            this.deviceProperties = deviceProperties;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceDto)) return false;
        DeviceDto deviceDto = (DeviceDto) o;
        return Objects.equals(deviceId, deviceDto.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceId);
    }

    @Override
    public String aggregateType() {
        return DeviceConstant.AGGREGATE_TYPE;
    }

    @Override
    public String aggregateId() {
        return deviceId.toString();
    }
}
