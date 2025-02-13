package cn.ecosync.ibms.gateway.model;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;

import static cn.ecosync.ibms.Constants.PATTERN_CODE;

@Getter
@ToString
public class DeviceDataPointLabel {
    private String name;
    private String value;

    protected DeviceDataPointLabel() {
    }

    public DeviceDataPointLabel(String name, String value) {
        this.name = name;
        this.value = value;
        Assert.isTrue(validate(), "");
    }

    @AssertTrue
    public boolean validate() {
        return PATTERN_CODE.matcher(name).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDataPointLabel that = (DeviceDataPointLabel) o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
