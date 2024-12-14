//package cn.ecosync.ibms.device.model;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import jakarta.validation.constraints.NotBlank;
//import lombok.Getter;
//import org.springframework.util.Assert;
//
//import java.util.Objects;
//
//@Getter
//@Embeddable
//public class DeviceSchemaId {
//    @NotBlank
//    @Column(name = "schame_name", nullable = false, updatable = false)
//    private String schemaName;
//
//    protected DeviceSchemaId() {
//    }
//
//    public DeviceSchemaId(String schemaName) {
//        Assert.hasText(schemaName, "schemaName can not be empty");
//        this.schemaName = schemaName;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof DeviceSchemaId)) return false;
//        DeviceSchemaId that = (DeviceSchemaId) o;
//        return Objects.equals(schemaName, that.schemaName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(schemaName);
//    }
//
//    @Override
//    public String toString() {
//        return schemaName;
//    }
//}
