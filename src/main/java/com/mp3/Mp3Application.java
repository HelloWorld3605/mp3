package com.mp3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Mp3Application {

    public static void main(String[] args) {
        SpringApplication.run(Mp3Application.class, args);
    }

}
