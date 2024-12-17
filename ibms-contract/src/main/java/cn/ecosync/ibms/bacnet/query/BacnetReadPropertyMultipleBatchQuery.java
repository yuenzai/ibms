package cn.ecosync.ibms.bacnet.query;

import cn.ecosync.ibms.bacnet.dto.BacnetReadPropertyMultipleService;
import cn.ecosync.ibms.bacnet.dto.ReadPropertyMultipleAck;
import cn.ecosync.iframework.query.Query;
import cn.ecosync.iframework.util.CollectionDelegate;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@Getter
@ToString
public class BacnetReadPropertyMultipleBatchQuery implements Query<List<ReadPropertyMultipleAck>>, CollectionDelegate<BacnetReadPropertyMultipleService> {
    @Valid
    @NotEmpty
    @JsonUnwrapped
    private List<BacnetReadPropertyMultipleService> services;

    protected BacnetReadPropertyMultipleBatchQuery() {
    }

    public BacnetReadPropertyMultipleBatchQuery(List<BacnetReadPropertyMultipleService> services) {
        this.services = services;
    }

    @Override
    public Collection<BacnetReadPropertyMultipleService> delegate() {
        return services;
    }
}
