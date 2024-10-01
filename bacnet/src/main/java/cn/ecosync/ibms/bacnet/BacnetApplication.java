package cn.ecosync.ibms.bacnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.ecosync.ibms")
public class BacnetApplication {
    public static void main(String[] args) {
        SpringApplication.run(BacnetApplication.class, args);
    }
}
