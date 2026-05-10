package com.example.coffeeorderproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoffeeOrderProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeOrderProjectApplication.class, args);
    }

}
