package org.example.wms_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("org.example.wms_backend.mapper")
@EnableScheduling
public class WmsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmsBackendApplication.class, args);
    }
}
