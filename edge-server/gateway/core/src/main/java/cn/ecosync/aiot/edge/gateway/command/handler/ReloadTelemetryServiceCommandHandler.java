package cn.ecosync.aiot.edge.gateway.command.handler;

import cn.ecosync.aiot.command.CommandHandler;
import cn.ecosync.aiot.edge.gateway.command.ReloadTelemetryServiceCommand;
import cn.ecosync.aiot.edge.gateway.service.GatewayApplicationService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.annotation.Transactional;

public class ReloadTelemetryServiceCommandHandler implements CommandHandler<ReloadTelemetryServiceCommand>, ApplicationRunner {
    private final GatewayApplicationService gatewayApplicationService;

    public ReloadTelemetryServiceCommandHandler(GatewayApplicationService gatewayApplicationService) {
        this.gatewayApplicationService = gatewayApplicationService;
    }

    @Override
    @Transactional(readOnly = true)
    public void handle(ReloadTelemetryServiceCommand command) {
        gatewayApplicationService.reloadTelemetryService();
    }

    @Override
    public void run(ApplicationArguments args) {
        gatewayApplicationService.reloadTelemetryService();
    }
}
