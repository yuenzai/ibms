package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.model.BacnetDeviceAddress;
import cn.ecosync.ibms.bacnet.model.BacnetWhoIsService;
import cn.ecosync.ibms.query.Query;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.List;

@Getter
@ToString
public class BacnetWhoIsQuery implements Query<List<BacnetDeviceAddress>> {
    @Valid
    @JsonUnwrapped
    private BacnetWhoIsService service;
}
