package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.device.command.AddDataAcquisitionCommand;
import cn.ecosync.ibms.device.command.RemoveDataAcquisitionCommand;
import cn.ecosync.ibms.device.command.UpdateDataAcquisitionCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.dto.DeviceDataAcquisitionView;
import cn.ecosync.ibms.device.query.GetDataAcquisitionQuery;
import cn.ecosync.ibms.device.query.SearchDataAcquisitionQuery;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.query.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device-daq")
public class DeviceDataAcquisitionWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    @PostMapping("/add")
    public void execute(@RequestBody @Validated AddDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/update")
    public void execute(@RequestBody @Validated UpdateDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/remove")
    public void execute(@RequestBody @Validated RemoveDataAcquisitionCommand command) {
        commandBus.execute(command);
    }

    @PostMapping("/get")
    public DeviceDataAcquisitionView get(@RequestBody @Validated GetDataAcquisitionQuery query) {
        DeviceDataAcquisition dataAcquisition = queryBus.execute(query);
        return new DeviceDataAcquisitionView(dataAcquisition, query.toPageable());
    }

    @PostMapping("/search")
    public Iterable<DeviceDataAcquisition> search(@RequestBody @Validated SearchDataAcquisitionQuery query) {
        List<DeviceDataAcquisition> list = queryBus.execute(query);
        Pageable pageable = query.toPageable();
        return pageable.isPaged() ? new PageImpl<>(list, pageable, list.size()) : list;
    }
}
