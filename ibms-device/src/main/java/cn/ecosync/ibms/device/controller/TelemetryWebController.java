package cn.ecosync.ibms.device.controller;

import cn.ecosync.ibms.command.CommandBus;
import cn.ecosync.ibms.device.command.ReloadPrometheusConfigurationCommand;
import cn.ecosync.ibms.device.model.DeviceDataAcquisition;
import cn.ecosync.ibms.device.model.TelemetryService;
import cn.ecosync.ibms.device.query.SearchDataAcquisitionQuery;
import cn.ecosync.ibms.query.QueryBus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Tag(name = "遥测服务")
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/telemetry")
public class TelemetryWebController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final TelemetryService telemetryService;

    @PostMapping(headers = "Command-Type=RELOAD")
    public void reload() {
        SearchDataAcquisitionQuery query = new SearchDataAcquisitionQuery();
        Page<DeviceDataAcquisition> page = queryBus.execute(query);
        List<DeviceDataAcquisition> dataAcquisitions = page.getContent();
        if (telemetryService != null) {
            telemetryService.set(dataAcquisitions);
            telemetryService.reload();
        }
        ReloadPrometheusConfigurationCommand command = new ReloadPrometheusConfigurationCommand(dataAcquisitions);
        commandBus.execute(command);
    }
}
