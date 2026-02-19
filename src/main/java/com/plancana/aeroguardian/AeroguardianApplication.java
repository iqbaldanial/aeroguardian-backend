package com.plancana.aeroguardian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AeroguardianApplication {

    public static void main(String[] args) {
        SpringApplication.run(AeroguardianApplication.class, args);
    }

}
