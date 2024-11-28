package cn.ecosync.ibms.bacnet.query;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ListSearchBacnetDeviceObjectIdsQuery {
    @NotNull
    private Integer deviceInstance;
}
