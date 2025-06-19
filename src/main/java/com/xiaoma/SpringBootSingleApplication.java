package com.xiaoma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.xiaoma")
@EnableScheduling
@EnableAsync
public class SpringBootSingleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSingleApplication.class, args);
    }
}