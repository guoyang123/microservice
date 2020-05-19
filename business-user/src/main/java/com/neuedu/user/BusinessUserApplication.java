package com.neuedu.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.neuedu.user.dao")
public class BusinessUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessUserApplication.class, args);
    }

}
