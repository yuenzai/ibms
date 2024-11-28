package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.dto.BacnetDeviceAddress;
import cn.ecosync.ibms.bacnet.dto.BacnetWhoIsService;
import cn.ecosync.iframework.query.Query;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class BacnetWhoIsQuery implements Query<List<BacnetDeviceAddress>> {
    @Valid
    @JsonUnwrapped
    private BacnetWhoIsService service;
}
