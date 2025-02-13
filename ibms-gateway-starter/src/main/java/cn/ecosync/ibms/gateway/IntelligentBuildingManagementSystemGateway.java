package cn.ecosync.ibms.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.ecosync.ibms")
public class IntelligentBuildingManagementSystemGateway {
    public static void main(String[] args) {
        SpringApplication.run(IntelligentBuildingManagementSystemGateway.class, args);
    }
}
