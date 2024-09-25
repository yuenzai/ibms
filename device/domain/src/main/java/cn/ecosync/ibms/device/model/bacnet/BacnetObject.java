package cn.ecosync.ibms.device.model.bacnet;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class BacnetObject {
    @NotNull
    private BACnetObjectType objectType;
    @NotEmpty
    private Integer objectInstance;
}
