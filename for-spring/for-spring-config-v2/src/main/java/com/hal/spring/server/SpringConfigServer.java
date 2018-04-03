package com.hal.spring.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SpringConfigServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringConfigServer.class, args);
    }

}
