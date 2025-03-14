package cn.ecosync.ibms.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.ecosync.ibms")
public class IntelligentBuildingManagementSystemServer {
    public static void main(String[] args) {
        SpringApplication.run(IntelligentBuildingManagementSystemServer.class, args);
    }
}
