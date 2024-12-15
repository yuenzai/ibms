package cn.ecosync.ibms.bacnet.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetObjectPropertyWithKey {
    @NotBlank
    private String key;
    @Valid
    @JsonUnwrapped
    private BacnetObjectProperty bop;

    protected BacnetObjectPropertyWithKey() {
    }

    public BacnetObject getBacnetObject() {
        return bop.getBacnetObject();
    }

    public BacnetProperty getBacnetProperty() {
        return bop.getBacnetProperty();
    }
}
