package cn.ecosync.aiot.edge.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.ecosync.aiot")
public class AIoTEdgeGateway {
    public static void main(String[] args) {
        SpringApplication.run(AIoTEdgeGateway.class, args);
    }
}
