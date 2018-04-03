package com.spring.eureka.client;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableEurekaClient
@RestController
public class EurekaClient {

    @RequestMapping("/")
    public String home(){
        return  "hello world!";
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaClient.class).web(true).run(args);
    }
}
