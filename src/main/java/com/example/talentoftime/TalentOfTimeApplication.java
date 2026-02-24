package com.example.talentoftime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TalentOfTimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalentOfTimeApplication.class, args);
    }

}
