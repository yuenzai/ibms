package cn.ecosync.ibms.gateway.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.gateway.command.ReloadTelemetryServiceCommand;
import cn.ecosync.ibms.gateway.service.TelemetryService;
import org.springframework.transaction.annotation.Transactional;

public class ReloadTelemetryServiceCommandHandler implements CommandHandler<ReloadTelemetryServiceCommand> {
    private final TelemetryService telemetryService;

    public ReloadTelemetryServiceCommandHandler(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @Override
    @Transactional(readOnly = true)
    public void handle(ReloadTelemetryServiceCommand command) {
        telemetryService.reload();
    }
}
