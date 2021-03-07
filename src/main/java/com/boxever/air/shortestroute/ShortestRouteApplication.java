package com.boxever.air.shortestroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ShortestRouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortestRouteApplication.class, args);
    }
}
