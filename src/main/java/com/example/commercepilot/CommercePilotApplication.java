package com.example.commercepilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CommercePilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommercePilotApplication.class, args);
    }

}
