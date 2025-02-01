package cn.ecosync.ibms.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@Embeddable
public class DeviceSchemasId {
    @NotBlank
    @Column(name = "schemas_code", nullable = false, updatable = false)
    private String schemasCode;

    protected DeviceSchemasId() {
    }

    public DeviceSchemasId(String schemasCode) {
        Assert.hasText(schemasCode, "schemasCode must not be null");
        this.schemasCode = schemasCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceSchemasId)) return false;
        DeviceSchemasId that = (DeviceSchemasId) o;
        return Objects.equals(this.schemasCode, that.schemasCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(schemasCode);
    }

    @Override
    public String toString() {
        return schemasCode;
    }
}
