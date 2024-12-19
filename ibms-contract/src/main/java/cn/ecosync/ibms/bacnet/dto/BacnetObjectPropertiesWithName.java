package cn.ecosync.ibms.bacnet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BacnetObjectPropertiesWithName extends BacnetObjectProperties {
    @NotBlank
    private String name;
}
