package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@Embeddable
public class DeviceSchemaId {
    @NotBlank
    @Column(name = "subject_name", nullable = false, updatable = false)
    private String subjectName;

    protected DeviceSchemaId() {
    }

    public DeviceSchemaId(String subjectName) {
        Assert.hasText(subjectName, "subjectName can not be empty");
        this.subjectName = subjectName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceSchemaId)) return false;
        DeviceSchemaId that = (DeviceSchemaId) o;
        return Objects.equals(subjectName, that.subjectName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(subjectName);
    }

    @Override
    public String toString() {
        return subjectName;
    }
}
