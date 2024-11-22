package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.model.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.model.ReadPropertyMultipleAck;
import cn.ecosync.iframework.query.Query;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@ToString
public class BacnetReadPropertyMultipleBatchQuery implements Query<List<ReadPropertyMultipleAck>> {
    @Valid
    @NotEmpty
    @JsonUnwrapped
    private List<BacnetReadPropertyMultipleService> services;

    protected BacnetReadPropertyMultipleBatchQuery() {
    }

    public BacnetReadPropertyMultipleBatchQuery(List<BacnetReadPropertyMultipleService> services) {
        this.services = services;
    }
}
